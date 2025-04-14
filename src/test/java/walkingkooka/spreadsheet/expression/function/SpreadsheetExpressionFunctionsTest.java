/*
 * Copyright 2022 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.convert.Converters;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.environment.AuditInfo;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.SpreadsheetErrorKind;
import walkingkooka.spreadsheet.SpreadsheetExpressionFunctionNames;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.convert.SpreadsheetConvertersConverterProviders;
import walkingkooka.spreadsheet.engine.SpreadsheetEngine;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineContext;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineContexts;
import walkingkooka.spreadsheet.engine.SpreadsheetEngines;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.store.SpreadsheetMetadataStore;
import walkingkooka.spreadsheet.provider.SpreadsheetProviders;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.security.store.SpreadsheetGroupStores;
import walkingkooka.spreadsheet.security.store.SpreadsheetUserStores;
import walkingkooka.spreadsheet.store.SpreadsheetCellRangeStores;
import walkingkooka.spreadsheet.store.SpreadsheetCellReferencesStores;
import walkingkooka.spreadsheet.store.SpreadsheetCellStores;
import walkingkooka.spreadsheet.store.SpreadsheetColumnStores;
import walkingkooka.spreadsheet.store.SpreadsheetExpressionReferenceStores;
import walkingkooka.spreadsheet.store.SpreadsheetLabelStores;
import walkingkooka.spreadsheet.store.SpreadsheetRowStores;
import walkingkooka.spreadsheet.store.repo.SpreadsheetStoreRepositories;
import walkingkooka.spreadsheet.store.repo.SpreadsheetStoreRepository;
import walkingkooka.spreadsheet.validation.form.store.SpreadsheetFormStores;
import walkingkooka.storage.StorageStores;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProvider;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.validation.ValidationError;

import java.lang.reflect.Method;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class SpreadsheetExpressionFunctionsTest implements PublicStaticHelperTesting<SpreadsheetExpressionFunctions>,
        SpreadsheetMetadataTesting,
        TreePrintableTesting {

    private final static Locale LOCALE = Locale.forLanguageTag("EN-AU");
    private final static AbsoluteUrl SERVER_URL = Url.parseAbsolute("https://server.example.com");
    private final static ExpressionFunctionProvider EXPRESSION_FUNCTION_PROVIDER = SpreadsheetExpressionFunctionProviders.expressionFunctionProvider(SpreadsheetExpressionFunctionNames.CASE_SENSITIVITY);

    // error handling tests............................................................................................

    @Test
    public void testEvaluateWithIncompleteExpression() {
        this.evaluateAndValueCheck(
                "=1+",
                SpreadsheetErrorKind.ERROR.setMessage(
                        "End of text at (4,1) expected LAMBDA_FUNCTION | NAMED_FUNCTION | \"true\" | \"false\" | LABEL | CELL_RANGE | CELL | GROUP | NEGATIVE | \"#.#E+#;#.#%;#.#;#%;#\" | TEXT | \"#NULL!\" | \"#DIV/0!\" | \"#VALUE!\" | \"#REF!\" | \"#NAME?\" | \"#NAME?\" | \"#NUM!\" | \"#N/A\" | \"#ERROR\" | \"#SPILL!\" | \"#CALC!\""
                )
        );
    }

    @Test
    public void testEvaluateWithEqMissingCell() {
        this.evaluateAndValueCheck(
                "=Z99",
                null
        );
    }

    @Test
    public void testEvaluateWithEqUnknownLabel() {
        this.evaluateAndValueCheck(
                "=Label123",
                SpreadsheetError.selectionNotFound(
                        SpreadsheetSelection.labelName("Label123")
                )
        );
    }

    @Test
    public void testEvaluateWithEqUnknownFunction() {
        this.evaluateAndValueCheck(
                "=UnknownFunction123()",
                SpreadsheetError.functionNotFound(
                        ExpressionFunctionName.with("UnknownFunction123")
                )
        );
    }

    @Test
    public void testEvaluateWithExpressionIncludesMissingCell() {
        this.evaluateAndValueCheck(
                "=123+Z99",
                EXPRESSION_NUMBER_KIND.create(123)
        );
    }

    // evaluateAndValueCheck tests......................................................................................

    @Test
    public void testEvaluateWithMathExpression() {
        this.evaluateAndValueCheck(
                "=1+2+3",
                EXPRESSION_NUMBER_KIND.create(1 + 2 + 3)
        );
    }

    @Test
    public void testEvaluateWithExpressionIncludingReferences() {
        this.evaluateAndValueCheck(
                "=1+A2+A3",
                Maps.of(
                        "A2", "=2",
                        "A3", "=3"
                ),
                EXPRESSION_NUMBER_KIND.create(1 + 2 + 3)
        );
    }

    @Test
    public void testEvaluateWithDivideByZero() {
        this.evaluateAndValueCheck(
                "=1/0",
                SpreadsheetErrorKind.DIV0.setMessage("Division by zero")
        );
    }

    @Test
    public void testEvaluateWithMathExpressionEvaluation() {
        this.evaluateAndValueCheck(
                "=1+A2+A3",
                Maps.of(
                        "A2", "=2"
                ),
                EXPRESSION_NUMBER_KIND.create(
                        1 + 2 + 0
                )
        );
    }

    @Test
    public void testEvaluateWithFunctionNameCaseInsensitive() {
        this.evaluateAndValueCheck(
                "=TRUE()",
                true
        );
    }

    // function tests...................................................................................................

    @Test
    public void testEvaluateAbs() {
        this.evaluateAndValueCheck(
                "=abs(-1.5)+abs(0.5)",
                EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testEvaluateAcos() {
        this.evaluateAndValueCheck(
                "=acos(0.5)",
                EXPRESSION_NUMBER_KIND.create(1.047198)
        );
    }

    @Test
    public void testEvaluateAddress() {
        this.evaluateAndValueCheck(
                "=address(1, 2)",
                SpreadsheetSelection.parseCell("$B$1")
        );
    }

    @Test
    public void testEvaluateAndTrueTrue() {
        this.evaluateAndValueCheck(
                "=and(true(), true())",
                Boolean.TRUE
        );
    }

    @Test
    public void testEvaluateAndTrue1() {
        this.evaluateAndValueCheck(
                "=and(true(), 1)",
                Boolean.TRUE
        );
    }

    @Test
    public void testEvaluateAndTrue0() {
        this.evaluateAndValueCheck(
                "=and(true(), 0)",
                Boolean.FALSE
        );
    }

    @Test
    public void testEvaluateAndTrueTrueTrue() {
        this.evaluateAndValueCheck(
                "=and(true(), true(), true())",
                Boolean.TRUE
        );
    }

    @Test
    public void testEvaluateAsin() {
        this.evaluateAndValueCheck(
                "=asin(0.5)",
                EXPRESSION_NUMBER_KIND.create(0.5235988)
        );
    }

    @Test
    public void testEvaluateAtan() {
        this.evaluateAndValueCheck(
                "=atan(0.5)",
                EXPRESSION_NUMBER_KIND.create(0.4636476)
        );
    }

    @Test
    public void testEvaluateAverage() {
        this.evaluateAndValueCheck(
                "=average(1,20,300,B1:D1)",
                Maps.of(
                        "B1", "1000",
                        "C1", "2000",
                        "D1", "3000"
                ),
                EXPRESSION_NUMBER_KIND.create(1053.5)
        );
    }

    @Test
    public void testEvaluateAverageIfSomeValuesFiltered() {
        this.evaluateAndValueCheck(
                "=averageIf(A2:A4, \">100\")",
                Maps.of(
                        "A2", "=1", //
                        "A3", "=200", //
                        "A4", "=\"400\"" // string with number converted
                ),
                EXPRESSION_NUMBER_KIND.create(600 / 2)
        );
    }

    @Test
    public void testEvaluateBase() {
        this.evaluateAndValueCheck(
                "=base(13, 2)",
                "1101"
        );
    }

    @Test
    public void testEvaluateBin2Dec() {
        this.evaluateAndValueCheck(
                "=bin2dec(\"1101\")",
                "13"
        );
    }

    @Test
    public void testEvaluateBin2Hex() {
        this.evaluateAndValueCheck(
                "=bin2hex(\"1101\")",
                "d"
        );
    }

    @Test
    public void testEvaluateBin2Oct() {
        this.evaluateAndValueCheck(
                "=bin2oct(\"1001\")",
                "11"
        );
    }

    @Test
    public void testEvaluateBitAnd() {
        this.evaluateAndValueCheck(
                "=bitand(14, 7)",
                EXPRESSION_NUMBER_KIND.create(6)
        );
    }

    @Test
    public void testEvaluateBitOr() {
        this.evaluateAndValueCheck(
                "=bitor(3, 6)",
                EXPRESSION_NUMBER_KIND.create(7)
        );
    }

    @Test
    public void testEvaluateBitXor() {
        this.evaluateAndValueCheck(
                "=bitxor(7, 3)",
                EXPRESSION_NUMBER_KIND.create(4)
        );
    }

    @Test
    public void testEvaluateCeil() {
        this.evaluateAndValueCheck(
                "=ceil(1.75)",
                EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testEvaluateCell() {
        this.evaluateAndValueCheck(
                "=cell(\"address\", B2)",
                Maps.of("b2", "=1*2"),
                SpreadsheetSelection.parseCell("B2")
        );
    }

    @Test
    public void testEvaluateCellValueMissing() {
        // cellValue cannot be used within a regular formula because it tries to get the value from itself.
        // it is intended to be only used within find/highlighting queries.
        this.evaluateAndValueCheck(
                "=cellValue()",
                Maps.of("A1", "=1*2*3*4"),
                null
        );
    }

    @Test
    public void testEvaluateCharWith65() {
        this.evaluateAndValueCheck(
                "=char(65)",
                'A'
        );
    }

    @Test
    public void testEvaluateChooseFirst() {
        this.evaluateAndValueCheck(
                "=choose(1, 111, 222, 333)",
                EXPRESSION_NUMBER_KIND.create(111)
        );
    }

    @Test
    public void testEvaluateChooseSecond() {
        this.evaluateAndValueCheck(
                "=choose(2, 111, 222, 333)",
                EXPRESSION_NUMBER_KIND.create(222)
        );
    }

    @Test
    public void testEvaluateChooseThird() {
        this.evaluateAndValueCheck(
                "=choose(3, 111, true(), \"Third\")",
                "Third"
        );
    }

    @Test
    public void testEvaluateClean() {
        this.evaluateAndValueCheck(
                "=clean(\"\t\nNeeds cleaning \r\")",
                "Needs cleaning "
        );
    }

    @Test
    public void testEvaluateCodeCapitalA() {
        this.evaluateAndValueCheck(
                "=code(\"A\")",
                EXPRESSION_NUMBER_KIND.create(65)
        );
    }

    @Test
    public void testEvaluateColumn() {
        this.evaluateAndValueCheck(
                "=column(C1)",
                EXPRESSION_NUMBER_KIND.create(3)
        );
    }

    @Test
    public void testEvaluateColumns() {
        this.evaluateAndValueCheck(
                "=columns(Z99)",
                EXPRESSION_NUMBER_KIND.create(1)
        );
    }

    @Test
    public void testEvaluateColumnsWithRange() {
        this.evaluateAndValueCheck(
                "=columns(B1:D1)",
                EXPRESSION_NUMBER_KIND.create(3)
        );
    }

    @Test
    public void testEvaluateConcatNumber() {
        this.evaluateAndValueCheck(
                "=concat(1.25)",
                this.metadataWithStrangeNumberFormatPattern(),
                "1.25"
        );
    }

    @Test
    public void testEvaluateConcatString() {
        this.evaluateAndValueCheck(
                "=concat(\"abc\")",
                this.metadataWithStrangeNumberFormatPattern(),
                "abc"
        );
    }

    @Test
    public void testEvaluateConcatSingleValues() {
        this.evaluateAndValueCheck(
                "=concat(A2,A3)",
                Maps.of("A2", "'abc", "A3", "'123"),
                this.metadataWithStrangeNumberFormatPattern(),
                "abc123"
        );
    }

    @Test
    public void testEvaluateConcatRange() {
        this.evaluateAndValueCheck(
                "=concat(A2:A3)",
                Maps.of("A2", "'abc", "A3", "'123"),
                this.metadataWithStrangeNumberFormatPattern(),
                "abc123"
        );
    }

    @Test
    public void testEvaluateConcatRangeIncludesNumbers() {
        this.evaluateAndValueCheck(
                "=concat(A2:A3)",
                Maps.of("A2", "'abc", "A3", "=123"),
                this.metadataWithStrangeNumberFormatPattern(),
                "abc123"
        );
    }

    @Test
    public void testEvaluateConcatRangeMissingCell() {
        this.evaluateAndValueCheck(
                "=concat(A2:A5)",
                Maps.of("A2", "'abc", "A4", "'123"),
                this.metadataWithStrangeNumberFormatPattern(),
                "abc123"
        );
    }

    @Test
    public void testEvaluateConcatSingleValuesAndRange() {
        this.evaluateAndValueCheck(
                "=concat(\"First\",A2:A3,\"!!!\",B1:B2)",
                Maps.of(
                        "A2", "'abc",
                        "A3", "'123",
                        "B1", "'SecondLast",
                        "B2", "'Last"
                ),
                this.metadataWithStrangeNumberFormatPattern(),
                "Firstabc123!!!SecondLastLast"
        );
    }

    @Test
    public void testEvaluateCos() {
        this.evaluateAndValueCheck(
                "=cos(1)",
                EXPRESSION_NUMBER_KIND.create(0.5403023)
        );
    }

    @Test
    public void testEvaluateCountWithDate() {
        this.evaluateAndValueCheck(
                "=count(date(1999, 12, 31))",
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateCountWithDateTime() {
        this.evaluateAndValueCheck(
                "=count(now())",
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateCountWithNumber() {
        this.evaluateAndValueCheck(
                "=count(1)",
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateCountWithString() {
        this.evaluateAndValueCheck(
                "=count(\"abc\")",
                EXPRESSION_NUMBER_KIND.zero()
        );
    }

    @Test
    public void testEvaluateCountWithStringConvertible() {
        this.evaluateAndValueCheck(
                "=count(\"123\")",
                EXPRESSION_NUMBER_KIND.zero()
        );
    }

    @Test
    public void testEvaluateCountWithTime() {
        this.evaluateAndValueCheck(
                "=count(time(12, 58, 59))",
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateCountWithRangeOfNumbers() {
        this.evaluateAndValueCheck(
                "=count(A2:A4)",
                Maps.of(
                        "A2", "=1",
                        "A3", "=20",
                        "A4", "=300"
                ),
                EXPRESSION_NUMBER_KIND.create(3)
        );
    }

    @Test
    public void testEvaluateCountWithRangeIncludesEmptyCells() {
        this.evaluateAndValueCheck(
                "=count(A2:A4)",
                Maps.of(
                        "A2", "=1",
                        "A4", "=300"
                ),
                EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testEvaluateCountWithRangeIncludesStrings() {
        this.evaluateAndValueCheck(
                "=count(A2:A4)",
                Maps.of(
                        "A2", "=1",
                        "A3", "=\"2\"", // string not converted
                        "A4", "=\"abc\""
                ),
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateCountWithRangeMixed() {
        this.evaluateAndValueCheck(
                "=count(A2:A7)",
                Maps.of(
                        "A2", "=1", // 1
                        "A3", "=today()", // 2 LocalDate
                        "A4", "=now()", // 3 LocalDateTime
                        "A5", "=\"2\"", // string with number - doesnt count
                        "A6", "=\"abc\""
                ),
                EXPRESSION_NUMBER_KIND.create(3)
        );
    }

    @Test
    public void testEvaluateCountAEmptyString() {
        this.evaluateAndValueCheck(
                "=countA(\"\")",
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateCountAMissingCell() {
        this.evaluateAndValueCheck(
                "=countA(Z99)", // becomes a #NAME which is ignored
                EXPRESSION_NUMBER_KIND.zero()
        );
    }

    @Test
    public void testEvaluateCountARangeOfMixedValues() {
        this.evaluateAndValueCheck(
                "=countA(A2:A8)",
                Maps.of(
                        "A2", "=1", // 1
                        "A3", "=today()", // 2 LocalDate
                        "A4", "=now()", // 3 LocalDateTime
                        "A5", "=\"2\"", // 4 string with number
                        "A6", "=\"abc\"", // 5
                        "A7", "\"\"" // 6
                ),
                EXPRESSION_NUMBER_KIND.create(6)
        );
    }

    @Test
    public void testEvaluateCountBlankEmptyString() {
        this.evaluateAndValueCheck(
                "=countBlank(\"\")",
                EXPRESSION_NUMBER_KIND.zero()
        );
    }

    @Test
    public void testEvaluateCountBlankNotEmptyString() {
        this.evaluateAndValueCheck(
                "=countBlank(\"not-empty\")",
                EXPRESSION_NUMBER_KIND.zero()
        );
    }

    @Test
    public void testEvaluateCountBlankMissingCell() {
        this.evaluateAndValueCheck(
                "=countBlank(Z99)", // becomes a #REF which counts as a non empty cell
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateCountBlankRangeOfMixedValues() {
        this.evaluateAndValueCheck(
                "=countBlank(A2:A8)",
                Maps.of(
                        "A2", "=1", //
                        "A3", "=today()", // LocalDate
                        "A4", "=now()", // LocalDateTime
                        "A5", "=\"2\"", // string with number
                        "A6", "=\"abc\"", //
                        "A8", "=\"\"" // not blank
                ),
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateCountIfOne() {
        this.evaluateAndValueCheck(
                "=countIf(123, 123)",
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateCountIfZero() {
        this.evaluateAndValueCheck(
                "=countIf(123, 456)",
                EXPRESSION_NUMBER_KIND.zero()
        );
    }

    @Test
    public void testEvaluateCountIfSomeValuesFiltered() {
        this.evaluateAndValueCheck(
                "=countIf(A2:A5, \">99+1\")",
                Maps.of(
                        "A2", "=1", //
                        "A3", "=2", //
                        "A4", "=now()", // will be > 100
                        "A5", "=\"200\"" // string are ignored
                ),
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateDate() {
        this.evaluateAndValueCheck(
                "=date(1999, 12, 31)",
                LocalDate.of(1999, 12, 31)
        );
    }

    @Test
    public void testEvaluateDay() {
        this.evaluateAndValueCheck(
                "=day(date(1999, 12, 31))",
                EXPRESSION_NUMBER_KIND.create(31)
        );
    }

    @Test
    public void testEvaluateDays() {
        this.evaluateAndValueCheck(
                "=days(date(2000, 1, 28), date(1999, 12, 31))",
                EXPRESSION_NUMBER_KIND.create(28)
        );
    }

    @Test
    public void testEvaluateDec2BinFromString() {
        this.evaluateAndValueCheck(
                "=dec2bin(\"14\")",
                "1110"
        );
    }

    @Test
    public void testEvaluateDec2BinFromNumber() {
        this.evaluateAndValueCheck(
                "=dec2bin(14)",
                "1110"
        );
    }

    @Test
    public void testEvaluateDec2Hex() {
        this.evaluateAndValueCheck(
                "=dec2hex(\"255\")",
                "ff"
        );
    }

    @Test
    public void testEvaluateDec2Oct() {
        this.evaluateAndValueCheck(
                "=dec2oct(\"255\")",
                "377"
        );
    }

    @Test
    public void testEvaluateDegrees() {
        this.evaluateAndValueCheck(
                "=degrees(1.5)",
                EXPRESSION_NUMBER_KIND.create(85.943655)
        );
    }

    @Test
    public void testEvaluateDecimal() {
        this.evaluateAndValueCheck(
                "=decimal(\"11\", 2)",
                EXPRESSION_NUMBER_KIND.create(3)
        );
    }

    @Test
    public void testEvaluateDeltaNumbersEquals() {
        this.evaluateAndValueCheck(
                "=delta(1.25, \"1.25\")",
                true
        );
    }

    @Test
    public void testEvaluateDollarWithNumberAndMissingDecimals() {
        this.evaluateAndValueCheck(
                "=dollar(123.4567)",
                "$123.46"
        );
    }

    @Test
    public void testEvaluateDollarWithStringAndMissingDecimals() {
        this.evaluateAndValueCheck(
                "=dollar(\"123.4567\")",
                "$123.46"
        );
    }

    @Test
    public void testEvaluateDollarWithNumberAndPlus2Decimals() {
        this.evaluateAndValueCheck(
                "=dollar(123.4567, 2)",
                "$123.46"
        );
    }

    @Test
    public void testEvaluateDollarWithNumberAndMinus2Decimals() {
        this.evaluateAndValueCheck(
                "=dollar(123.4567, -2)",
                "$100"
        );
    }

    @Test
    public void testEvaluateE() {
        this.evaluateAndValueCheck(
                "=e()",
                EXPRESSION_NUMBER_KIND.create(2.718282)
        );
    }

    @Test
    public void testEvaluateEval() {
        this.evaluateAndValueCheck(
                "=eval(\"11+22\")",
                EXPRESSION_NUMBER_KIND.create(
                        11 + 22
                )
        );
    }

    @Test
    public void testEvaluateEven() {
        this.evaluateAndValueCheck(
                "=even(1.7)",
                EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testEvaluateExactDifferentCaseStrings() {
        this.evaluateAndValueCheck(
                "=exact(\"ABC\", \"abc\")",
                false
        );
    }

    @Test
    public void testEvaluateExactSameStrings() {
        this.evaluateAndValueCheck(
                "=exact(\"ABC\", \"ABC\")",
                true
        );
    }

    @Test
    public void testEvaluateExactSameString2() {
        this.evaluateAndValueCheck(
                "=exact(\"12.5\",12.5)",
                true
        );
    }

    @Test
    public void testEvaluateExp() {
        this.evaluateAndValueCheck(
                "=exp(1)",
                EXPRESSION_NUMBER_KIND.create(2.718282)
        );
    }

    @Test
    public void testEvaluateFalse() {
        this.evaluateAndValueCheck(
                "=false()",
                Boolean.FALSE
        );
    }

    @Test
    public void testEvaluateFindFound() {
        this.evaluateAndValueCheck(
                "=find(\"abc\", \"before abc\")",
                EXPRESSION_NUMBER_KIND.create(1 + "before ".length())
        );
    }

    @Test
    public void testEvaluateFindNotFound() {
        this.evaluateAndValueCheck(
                "=find(\"Not found\", \"123\")",
                SpreadsheetErrorKind.VALUE.setMessage("\"Not found\" not found in \"123\"")
        );
    }

    @Test
    public void testEvaluateFixedWithNumber() {
        this.evaluateAndValueCheck(
                "=fixed(123.456)",
                "123.46"
        );
    }

    @Test
    public void testEvaluateFixedWithNumberAndDecimals() {
        this.evaluateAndValueCheck(
                "=fixed(123.567, 1)",
                "123.6"
        );
    }

    @Test
    public void testEvaluateFixedWithNumberAndDecimalsAndCommas() {
        this.evaluateAndValueCheck(
                "=fixed(1234.567, 1, false())",
                "1,234.6"
        );
    }

    @Test
    public void testEvaluateFloor() {
        this.evaluateAndValueCheck(
                "=floor(1.8)",
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateFormulaText() {
        this.evaluateAndValueCheck(
                "=formulatext(A2)",
                Maps.of("A2", "=1+2+3"),
                "=1+2+3"
        );
    }

    @Test
    public void testEvaluateHex2Bin() {
        this.evaluateAndValueCheck(
                "=hex2bin(\"f\")",
                "1111"
        );
    }

    @Test
    public void testEvaluateHex2Dec() {
        this.evaluateAndValueCheck(
                "=hex2dec(\"ff\")",
                "255"
        );
    }

    @Test
    public void testEvaluateHex2Oct() {
        this.evaluateAndValueCheck(
                "=hex2oct(\"ff\")",
                "377"
        );
    }

    @Test
    public void testEvaluateHour() {
        this.evaluateAndValueCheck(
                "=hour(time(12, 58, 59))",
                EXPRESSION_NUMBER_KIND.create(12)
        );
    }

    @Test
    public void testEvaluateHyperlink() {
        this.evaluateAndValueCheck(
                "=hyperlink(\"A12\")",
                Url.parseAbsolute("https://server.example.com/1234/Untitled5678/cell/A12")
        );
    }

    @Test
    public void testEvaluateIfTrue() {
        this.evaluateAndValueCheck(
                "=if(true(), 111, 222)",
                EXPRESSION_NUMBER_KIND.create(111)
        );
    }

    @Test
    public void testEvaluateIfTrueCaseInsensitiveStringCompare() {
        this.evaluateAndValueCheck(
                "=if(\"abc\" = \"ABC\", 111, 222)",
                EXPRESSION_NUMBER_KIND.create(111)
        );
    }

    @Test
    public void testEvaluateIfTrueCaseInsensitiveStringCompareDifferent() {
        this.evaluateAndValueCheck(
                "=if(\"abc\" = \"different\", 111, 222)",
                EXPRESSION_NUMBER_KIND.create(222)
        );
    }

    @Test
    public void testEvaluateIfFalse() {
        this.evaluateAndValueCheck(
                "=if(false(), 111, 222)",
                EXPRESSION_NUMBER_KIND.create(222)
        );
    }

    @Test
    public void testEvaluateIfsFirst() {
        this.evaluateAndValueCheck(
                "=ifs(true(), 111, false(), 222)",
                EXPRESSION_NUMBER_KIND.create(111)
        );
    }

    @Test
    public void testEvaluateIfsSecond() {
        this.evaluateAndValueCheck(
                "=ifs(\"abc\"=\"different\", 111, true(), 222)",
                EXPRESSION_NUMBER_KIND.create(222)
        );
    }

    @Test
    public void testEvaluateIfsSecondStringCaseInsensitiveEquals() {
        this.evaluateAndValueCheck(
                "=ifs(\"abc\"=\"different\", 111, \"same\"=\"SAME\", 222)",
                EXPRESSION_NUMBER_KIND.create(222)
        );
    }

    @Test
    public void testEvaluateIndirect() {
        this.evaluateAndValueCheck(
                "=indirect(\"Z99\")",
                SpreadsheetSelection.parseCell("Z99")
        );
    }

    @Test
    public void testEvaluateInt() {
        this.evaluateAndValueCheck(
                "=int(1.8)",
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateIsBlankNoCell() {
        this.evaluateAndValueCheck(
                "=isBlank(B2)",
                true
        );
    }

    @Test
    public void testEvaluateIsBlankCell() {
        this.evaluateAndValueCheck(
                "=isBlank(B2)",
                Maps.of("B2", "'NotBlank"),
                false
        );
    }

    @Test
    public void testEvaluateIsDateWithDate() {
        this.evaluateAndValueCheck(
                "=isDate(today())",
                true
        );
    }

    @Test
    public void testEvaluateIsDateWithNumber() {
        this.evaluateAndValueCheck(
                "=isDate(1)",
                true
        );
    }

    @Test
    public void testEvaluateIsDateWithString() {
        this.evaluateAndValueCheck(
                "=isDate(\"31/12/2000\")",
                true
        );
    }

    @Test
    public void testEvaluateIsDateWithTime() {
        this.evaluateAndValueCheck(
                "=isDate(time(1,1,1))",
                true
        );
    }

    @Test
    public void testEvaluateIsErrWithError() {
        this.evaluateAndValueCheck(
                "=isErr(1/0)",
                true
        );
    }

    @Test
    public void testEvaluateIsErrWithErrorRef() {
        this.evaluateAndValueCheck(
                "=isErr(#REF!)",
                true
        );
    }

    @Test
    public void testEvaluateIsErrWithNumber() {
        this.evaluateAndValueCheck(
                "=isErr(123)",
                false
        );
    }

    @Test
    public void testEvaluateIsErrorWithErroror() {
        this.evaluateAndValueCheck(
                "=isError(1/0)",
                true
        );
    }

    @Test
    public void testEvaluateIsErrorWithNumber() {
        this.evaluateAndValueCheck(
                "=isError(123)",
                false
        );
    }

    @Test
    public void testEvaluateIsEvenWithEvenNumber() {
        this.evaluateAndValueCheck(
                "=isEven(2)",
                true
        );
    }

    @Test
    public void testEvaluateIsEvenWithOddNumber() {
        this.evaluateAndValueCheck(
                "=isEven(1)",
                false
        );
    }

    @Test
    public void testEvaluateIsFormulaWithCellWithFormula() {
        this.evaluateAndValueCheck(
                "=isFormula(B2)",
                Maps.of("B2", "=1"),
                true
        );
    }

    @Test
    public void testEvaluateIsFormulaWithMissingCell() {
        this.evaluateAndValueCheck(
                "=isFormula(B2)",
                false
        );
    }

    @Test
    public void testEvaluateIsLogicalWithBooleanTrue() {
        this.evaluateAndValueCheck(
                "=isLogical(true())",
                true
        );
    }

    @Test
    public void testEvaluateIsLogicalWithBooleanFalse() {
        this.evaluateAndValueCheck(
                "=isLogical(false())",
                true
        );
    }

    @Test
    public void testEvaluateIsLogicalWithMissingCell() {
        this.evaluateAndValueCheck(
                "=isLogical(B2)",
                false
        );
    }

    @Test
    public void testEvaluateIsLogicalWithNumber() {
        this.evaluateAndValueCheck(
                "=isLogical(123)",
                false
        );
    }

    @Test
    public void testEvaluateIsLogicalWithString() {
        this.evaluateAndValueCheck(
                "=isLogical(\"abc\")",
                false
        );
    }

    @Test
    public void testEvaluateIsNaWithError() {
        this.evaluateAndValueCheck(
                "=isNa(1/0)",
                false
        );
    }

    @Test
    public void testEvaluateIsNaWithNumber() {
        this.evaluateAndValueCheck(
                "=isNa(123)",
                false
        );
    }

    @Test
    public void testEvaluateIsNonTextWithEmptyCell() {
        this.evaluateAndValueCheck(
                "=isNonText(Z99)",
                true
        );
    }

    @Test
    public void testEvaluateIsNonTextWithError() {
        this.evaluateAndValueCheck(
                "=isNonText(1/0)",
                true
        );
    }

    @Test
    public void testEvaluateIsNonTextWithString() {
        this.evaluateAndValueCheck(
                "=isNonText(\"abc\")",
                false
        );
    }

    @Test
    public void testEvaluateIsNullWithBoolean() {
        this.evaluateAndValueCheck(
                "=isNull(true)",
                false
        );
    }

    @Test
    public void testEvaluateIsNullWithErrorDivideByZero() {
        this.evaluateAndValueCheck(
                "=isNull(1/0)",
                false
        );
    }

    @Test
    public void testEvaluateIsNullWithNullValue() {
        this.evaluateAndValueCheck(
                "=isNull(null())",
                true
        );
    }

    @Test
    public void testEvaluateIsNullWithNumber() {
        this.evaluateAndValueCheck(
                "=isNull(123)",
                false
        );
    }

    @Test
    public void testEvaluateIsNullWithReference() {
        this.evaluateAndValueCheck(
                "=isNull(A2)",
                false
        );
    }

    @Test
    public void testEvaluateIsNullWithText() {
        this.evaluateAndValueCheck(
                "=isNull(\"Abc\")",
                false
        );
    }

    @Test
    public void testEvaluateIsNumberWithNonNumberString() {
        this.evaluateAndValueCheck(
                "=isNumber(\"ABC\")",
                false
        );
    }

    @Test
    public void testEvaluateIsNumberWithNumber() {
        this.evaluateAndValueCheck(
                "=isNumber(2)",
                true
        );
    }

    @Test
    public void testEvaluateIsOddWithEvenNumber() {
        this.evaluateAndValueCheck(
                "=isOdd(2)",
                false
        );
    }

    @Test
    public void testEvaluateIsOddWithOddNumber() {
        this.evaluateAndValueCheck(
                "=isOdd(1)",
                true
        );
    }

    @Test
    public void testEvaluateIsoWeekNum() {
        this.evaluateAndValueCheck(
                "=isoWeekNum(date(1999,12,31))",
                EXPRESSION_NUMBER_KIND.create(52)
        );
    }

    @Test
    public void testEvaluateIsRefWithNull() {
        this.evaluateAndValueCheck(
                "=isRef(nullValue())",
                false
        );
    }

    @Test
    public void testEvaluateIsRefWithNumber() {
        this.evaluateAndValueCheck(
                "=isRef(123)",
                false
        );
    }

    @Test
    public void testEvaluateIsRefWithCellReference() {
        this.evaluateAndValueCheck(
                "=isRef(A2)",
                true
        );
    }

    @Test
    public void testEvaluateIsRefWithStringWithCellReference() {
        this.evaluateAndValueCheck(
                "=isRef(\"A1\")",
                false
        );
    }

    @Test
    public void testEvaluateIsRefWithCellUnknownLabel() {
        this.evaluateAndValueCheck(
                "=isRef(UnknownLabel)",
                true
        );
    }

    @Test
    public void testEvaluateIsTextWithError() {
        this.evaluateAndValueCheck(
                "=isText(1/0)",
                false
        );
    }

    @Test
    public void testEvaluateIsTextWithNumber() {
        this.evaluateAndValueCheck(
                "=isText(123)",
                false
        );
    }

    @Test
    public void testEvaluateIsTextWithEmptyString() {
        this.evaluateAndValueCheck(
                "=isText(\"\")",
                true
        );
    }

    @Test
    public void testEvaluateIsTextWithString() {
        this.evaluateAndValueCheck(
                "=isText(\"abc\")",
                true
        );
    }

    @Test
    public void testEvaluateLambdaWithParameters() {
        this.evaluateAndValueCheck(
                "=lambda(x,y,x*y)(10,20)",
                EXPRESSION_NUMBER_KIND.create(10 * 20)
        );
    }

    @Test
    public void testEvaluateLAMBDAWithParameters() {
        this.evaluateAndValueCheck(
                "=LAMBDA(x,y,z,x*y*z)(20,30, 40)",
                EXPRESSION_NUMBER_KIND.create(20 * 30 * 40)
        );
    }

    @Test
    public void testEvaluateLambdaWithParametersAndCellReference() {
        this.evaluateAndValueCheck(
                "=lambda(x,y,x*y*b2)(10,20)",
                Maps.of("b2", "30"),
                EXPRESSION_NUMBER_KIND.create(10 * 20 * 30)
        );
    }

    // https://www.microsoft.com/en-us/research/blog/lambda-the-ultimatae-excel-worksheet-function/

    @Test
    public void testEvaluateLambdaWithParametersLet() {
        this.evaluateAndValueCheck(
                "=lambda(x,y,    Let(xs, x*x, ys, y*y, xs+ys))(3, 4)",
                EXPRESSION_NUMBER_KIND.create(25)
        );
    }

    @Test
    public void testEvaluateLambdaWithParametersLet2() {
        this.evaluateAndValueCheck(
                "=lambda(x,y,    Let(xs, x*x, ys, y*y, sqrt(xs+ys)))(3, 4)",
                EXPRESSION_NUMBER_KIND.create(5)
        );
    }

    @Test
    public void testEvaluateLeftMissingCellReference() {
        this.evaluateAndValueCheck(
                "=left(Z99)",
                SpreadsheetErrorKind.VALUE.toError()
        );
    }

    @Test
    public void testEvaluateLeft() {
        this.evaluateAndValueCheck(
                "=left(\"abc\")",
                "a"
        );
    }

    @Test
    public void testEvaluateLeft2() {
        this.evaluateAndValueCheck(
                "=left(\"abc\", 2)",
                "ab"
        );
    }

    @Test
    public void testEvaluateLenWithNumber() {
        this.evaluateAndValueCheck(
                "=len(1.23)",
                this.metadataWithStrangeNumberFormatPattern(),
                EXPRESSION_NUMBER_KIND.create(4)
        );
    }

    @Test
    public void testEvaluateLenWithString() {
        this.evaluateAndValueCheck(
                "=len(\"hello\")",
                this.metadataWithStrangeNumberFormatPattern(),
                EXPRESSION_NUMBER_KIND.create(5)
        );
    }

    @Test
    public void testEvaluateLetOnlyStringLiteral() {
        this.evaluateAndValueCheck(
                "=let(\"hello\")",
                "hello"
        );
    }

    @Test
    public void testEvaluateLetWithBackReferences() {
        this.evaluateAndValueCheck(
                "=let(x, 2, x * 3)",
                EXPRESSION_NUMBER_KIND.create(2 * 3)
        );
    }

    @Test
    public void testEvaluateLetWithBackReferences2() {
        this.evaluateAndValueCheck(
                "=let(x, 2, y, 3, x * y * x * y)",
                EXPRESSION_NUMBER_KIND.create(2 * 3 * 2 * 3)
        );
    }

    @Test
    public void testEvaluateLn() {
        this.evaluateAndValueCheck(
                "=ln(2)",
                EXPRESSION_NUMBER_KIND.create(0.6931472)
        );
    }

    @Test
    public void testEvaluateLog() {
        this.evaluateAndValueCheck(
                "=log(3, 2)",
                EXPRESSION_NUMBER_KIND.create(1.584962)
        );
    }

    @Test
    public void testEvaluateLog10() {
        this.evaluateAndValueCheck(
                "=log10(100)",
                EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testEvaluateLowerWithNumber() {
        this.evaluateAndValueCheck(
                "=lower(1.25)",
                this.metadataWithStrangeNumberFormatPattern(),
                "1.25"
        );
    }

    @Test
    public void testEvaluateLowerWithString() {
        this.evaluateAndValueCheck(
                "=lower(\"ABCxyz\")",
                this.metadataWithStrangeNumberFormatPattern(),
                "abcxyz"
        );
    }

    @Test
    public void testEvaluateMax() {
        this.evaluateAndValueCheck(
                "=max(1,20,300,B1:D1)",
                Maps.of(
                        "B1", "1000",
                        "C1", "2000",
                        "D1", "9999"
                ),
                EXPRESSION_NUMBER_KIND.create(9999)
        );
    }

    @Test
    public void testEvaluateMaxIf() {
        this.evaluateAndValueCheck(
                "=maxIf(A2:A4, \">=19\")",
                Maps.of(
                        "A2", "=1",
                        "A3", "=20",
                        "A4", "=\"400\""
                ),
                EXPRESSION_NUMBER_KIND.create(400)
        );
    }

    @Test
    public void testEvaluateMid() {
        this.evaluateAndValueCheck(
                "=mid(\"apple\", 2, 3)",
                "ppl"
        );
    }

    @Test
    public void testEvaluateMin() {
        this.evaluateAndValueCheck(
                "=min(1,20,300,B1:D1)",
                Maps.of(
                        "B1", "1000",
                        "C1", "2000",
                        "D1", "-999"
                ),
                EXPRESSION_NUMBER_KIND.create(-999)
        );
    }

    @Test
    public void testEvaluateMinIf() {
        this.evaluateAndValueCheck(
                "=minIf(A2:A4, \">=19\")",
                Maps.of(
                        "A2", "=1",
                        "A3", "=20",
                        "A4", "=\"400\""
                ),
                EXPRESSION_NUMBER_KIND.create(20)
        );
    }

    @Test
    public void testEvaluateMinute() {
        this.evaluateAndValueCheck(
                "=minute(time(12, 58, 59))",
                EXPRESSION_NUMBER_KIND.create(58)
        );
    }

    @Test
    public void testEvaluateMod() {
        this.evaluateAndValueCheck(
                "=mod(5, 3)",
                EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testEvaluateMonth() {
        this.evaluateAndValueCheck(
                "=month(date(1999, 12, 31))",
                EXPRESSION_NUMBER_KIND.create(12)
        );
    }

    @Test
    public void testEvaluateNextEmptyColumn() {
        this.evaluateAndValueCheck(
                "=nextEmptyColumn(\"2\")",
                SpreadsheetSelection.parseColumn("A")
        );
    }

    @Test
    public void testEvaluateNextEmptyRow() {
        this.evaluateAndValueCheck(
                "=nextEmptyRow(\"A\")",
                SpreadsheetSelection.parseRow("2")
        );
    }

    @Test
    public void testEvaluateNotFalse() {
        this.evaluateAndValueCheck(
                "=not(false())",
                Boolean.TRUE
        );
    }

    @Test
    public void testEvaluateNotTrue() {
        this.evaluateAndValueCheck(
                "=not(true())",
                Boolean.FALSE
        );
    }

    @Test
    public void testEvaluateNotZero() {
        this.evaluateAndValueCheck(
                "=not(0)",
                Boolean.TRUE
        );
    }


    @Test
    public void testEvaluateNow() {
        this.evaluateAndValueCheck(
                "=now()",
                NOW.now()
        );
    }

    @Test
    public void testEvaluateNumberValue() {
        this.evaluateAndValueCheck(
                "=numberValue(\"1G234D5\", \"D\", \"G\")",
                EXPRESSION_NUMBER_KIND.create(1234.5)
        );
    }

    @Test
    public void testEvaluateOct2Bin() {
        this.evaluateAndValueCheck(
                "=oct2bin(\"34\")",
                "11100"
        );
    }

    @Test
    public void testEvaluateOct2Dec() {
        this.evaluateAndValueCheck(
                "=oct2dec(\"34\")",
                "28"
        );
    }

    @Test
    public void testEvaluateOct2Hex() {
        this.evaluateAndValueCheck(
                "=oct2hex(\"34\")",
                "1c"
        );
    }

    @Test
    public void testEvaluateOdd() {
        this.evaluateAndValueCheck(
                "=odd(12.3)",
                EXPRESSION_NUMBER_KIND.create(13)
        );
    }

    @Test
    public void testEvaluateOffset() {
        this.evaluateAndValueCheck(
                "=offset(B2,1,2,3,3)",
                SpreadsheetSelection.parseCellRange("D3:F5")
        );
    }

    @Test
    public void testEvaluateOrTrueTrueTrue() {
        this.evaluateAndValueCheck(
                "=or(true(), true(), true())",
                Boolean.TRUE
        );
    }

    @Test
    public void testEvaluateOrFalseFalseTrue() {
        this.evaluateAndValueCheck(
                "=or(false(), false(), true())",
                Boolean.TRUE
        );
    }

    @Test
    public void testEvaluateOrFalseFalseFalseFalse() {
        this.evaluateAndValueCheck(
                "=or(false(), false(), false(), false())",
                Boolean.FALSE
        );
    }

    @Test
    public void testEvaluatePi() {
        this.evaluateAndValueCheck(
                "=pi()",
                EXPRESSION_NUMBER_KIND.create(3.141593)
        );
    }

    @Test
    public void testEvaluateProduct() {
        this.evaluateAndValueCheck(
                "=product(2, 5)",
                EXPRESSION_NUMBER_KIND.create(10)
        );
    }

    @Test
    public void testEvaluateProper() {
        this.evaluateAndValueCheck(
                "=proper(\"apple\")",
                "Apple"
        );
    }

    @Test
    public void testEvaluateProper2() {
        this.evaluateAndValueCheck(
                "=proper(\"apple, pears\")",
                "Apple, Pears"
        );
    }

    @Test
    public void testEvaluateQuotient() {
        this.evaluateAndValueCheck(
                "=quotient(12, 3)",
                EXPRESSION_NUMBER_KIND.create(4)
        );
    }

    @Test
    public void testEvaluateRadians() {
        this.evaluateAndValueCheck(
                "=radians(90)",
                EXPRESSION_NUMBER_KIND.create(1.5707961)
        );
    }

    @Test
    public void testEvaluateRand() {
        this.evaluateAndValueCheck(
                "=rand() > 0",
                true
        );
    }

    @Test
    public void testEvaluateRandBetween() {
        this.evaluateAndValueCheck(
                "=randBetween(2, 34) >= 2",
                true
        );
    }

    @Test
    public void testEvaluateReplace() {
        this.evaluateAndValueCheck(
                "=replace(\"XYZ123\",4,3,\"456\")",
                "XYZ456"
        );
    }

    @Test
    public void testEvaluateRept() {
        this.evaluateAndValueCheck(
                "=rept(\"abc\", 3)",
                "abcabcabc"
        );
    }

    @Test
    public void testEvaluateRight() {
        this.evaluateAndValueCheck(
                "=right(\"abc\")",
                "c"
        );
    }

    @Test
    public void testEvaluateRight2() {
        this.evaluateAndValueCheck(
                "=right(\"abc\", 2)",
                "bc"
        );
    }

    @Test
    public void testEvaluateRoman() {
        this.evaluateAndValueCheck(
                "=roman(123)",
                "CXXIII"
        );
    }

    @Test
    public void testEvaluateRound() {
        this.evaluateAndValueCheck(
                "=round(5.7845, 1)",
                EXPRESSION_NUMBER_KIND.create(5.8)
        );
    }

    @Test
    public void testEvaluateRoundDown() {
        this.evaluateAndValueCheck(
                "=roundDown(1.25, 1)",
                EXPRESSION_NUMBER_KIND.create(1.2)
        );
    }

    @Test
    public void testEvaluateRoundUp() {
        this.evaluateAndValueCheck(
                "=roundUp(1.25, 1)",
                EXPRESSION_NUMBER_KIND.create(1.3)
        );
    }

    @Test
    public void testEvaluateRow() {
        this.evaluateAndValueCheck(
                "=row(A99)",
                EXPRESSION_NUMBER_KIND.create(99)
        );
    }

    @Test
    public void testEvaluateRowsWithCell() {
        this.evaluateAndValueCheck(
                "=rows(Z99)",
                EXPRESSION_NUMBER_KIND.create(1)
        );
    }

    @Test
    public void testEvaluateRowsWithRange() {
        this.evaluateAndValueCheck(
                "=rows(B1:D1)",
                EXPRESSION_NUMBER_KIND.create(1)
        );
    }

    @Test
    public void testEvaluateRowsWithRange2() {
        this.evaluateAndValueCheck(
                "=rows(B3:D6)",
                EXPRESSION_NUMBER_KIND.create(4)
        );
    }

    @Test
    public void testEvaluateSearchCaseWithInsensitiveFound() {
        this.evaluateAndValueCheck(
                "=search(\"bc\", \"ABCDE\")",
                EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testEvaluateSearchCaseWithSensitiveFound() {
        this.evaluateAndValueCheck(
                "=search(\"bc\", \"abcde\")",
                EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testEvaluateSearchCaseWithQuestionMark() {
        this.evaluateAndValueCheck(
                "=search(\"1?3\", \"before 123 after\")",
                EXPRESSION_NUMBER_KIND.create(1 + "before ".length())
        );
    }

    @Test
    public void testEvaluateSearchCaseWithQuestionMark2() {
        this.evaluateAndValueCheck(
                "=search(\"1?3\", \"before 111 123 after\")",
                EXPRESSION_NUMBER_KIND.create(1 + "before 111 ".length())
        );
    }

    @Test
    public void testEvaluateSearchCaseWithWildcard() {
        this.evaluateAndValueCheck(
                "=search(\"1*4\", \"before 1234 after\")",
                EXPRESSION_NUMBER_KIND.create(1 + "before ".length())
        );
    }

    @Test
    public void testEvaluateSearchNotFound() {
        this.evaluateAndValueCheck(
                "=search(\"!\", \"abcde\")",
                EXPRESSION_NUMBER_KIND.create(0)
        );
    }

    @Test
    public void testEvaluateSecond() {
        this.evaluateAndValueCheck(
                "=second(time(12, 58, 59))",
                EXPRESSION_NUMBER_KIND.create(59)
        );
    }

    @Test
    public void testEvaluateSignWithNegativeNumber() {
        this.evaluateAndValueCheck(
                "=sign(-123)",
                EXPRESSION_NUMBER_KIND.create(-1)
        );
    }

    @Test
    public void testEvaluateSignWithZero() {
        this.evaluateAndValueCheck(
                "=sign(0)",
                EXPRESSION_NUMBER_KIND.zero()
        );
    }

    @Test
    public void testEvaluateSignWithPositiveNumber() {
        this.evaluateAndValueCheck(
                "=sign(+123)",
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateSin() {
        this.evaluateAndValueCheck(
                "=sin(1)",
                EXPRESSION_NUMBER_KIND.create(0.841471)
        );
    }

    @Test
    public void testEvaluateSinh() {
        this.evaluateAndValueCheck(
                "=sinh(1)",
                EXPRESSION_NUMBER_KIND.create(1.175201)
        );
    }

    @Test
    public void testEvaluateSpreadsheetMetadataGet() {
        this.evaluateAndValueCheck(
                "=spreadsheetMetadataGet(\"spreadsheetName\", \"missing!!!\")",
                SpreadsheetName.with("Untitled5678")
        );
    }

    @Test
    public void testEvaluateSpreadsheetMetadataSet() {
        final SpreadsheetName expected = SpreadsheetName.with("NewName222");

        final SpreadsheetEngineContext context = this.evaluateAndValueCheck(
                "=spreadsheetMetadataSet(\"spreadsheetName\", \"NewName222\")",
                expected
        );

        // must load SpreadsheetMetadata because BasicSpreadsheetEngineContext#spreadsheetMetadata is not refreshed after spreadsheet save.
        final SpreadsheetId id = context.spreadsheetMetadata()
                .getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID);

        this.checkEquals(
                expected,
                context.storeRepository()
                        .metadatas()
                        .loadOrFail(id)
                        .getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME),
                "spreadsheetMetadata"
        );
    }

    @Test
    public void testEvaluateSqrtWithNegativeNumber() {
        this.evaluateAndValueCheck(
                "=sqrt(-1)",
                SpreadsheetErrorKind.VALUE.setMessage("Illegal sqrt(x) for x < 0: x = -1")
        );
    }

    @Test
    public void testEvaluateSqrtWithPositiveNumber() {
        this.evaluateAndValueCheck(
                "=sqrt(100)",
                EXPRESSION_NUMBER_KIND.create(10)
        );
    }

    @Test
    public void testEvaluateSubstitute() {
        this.evaluateAndValueCheck(
                "=substitute(\"123-456-7890\",\"-\",\"\") ",
                "1234567890"
        );
    }

    @Test
    public void testEvaluateSum() {
        this.evaluateAndValueCheck(
                "=sum(1,20,300,B1:D1)",
                Maps.of(
                        "B1", "1000",
                        "C1", "2000",
                        "D1", "9999"
                ),
                EXPRESSION_NUMBER_KIND.create(13320)
        );
    }

    @Test
    public void testEvaluateSumMissingCell() {
        this.evaluateAndValueCheck(
                "=sum(B2)",
                EXPRESSION_NUMBER_KIND.zero()
        );
    }

    @Test
    public void testEvaluateSumMissingCell2() {
        this.evaluateAndValueCheck(
                "=sum(123+B2)",
                EXPRESSION_NUMBER_KIND.create(123)
        );
    }

    @Test
    public void testEvaluateSumMissingCellRange() {
        this.evaluateAndValueCheck(
                "=sum(B2:B3)",
                EXPRESSION_NUMBER_KIND.zero()
        );
    }

    @Test
    public void testEvaluateSumMissingCellRange2() {
        this.evaluateAndValueCheck(
                "=sum(B2:B3,123)",
                EXPRESSION_NUMBER_KIND.create(123)
        );
    }

    @Test
    public void testEvaluateSumMissingCellRange3() {
        this.evaluateAndValueCheck(
                "=sum(123,B2:B3)",
                Maps.of(
                        "B2", "1000"
                ),
                EXPRESSION_NUMBER_KIND.create(123 + 1000)
        );
    }

    @Test
    public void testEvaluateSumIfOne() {
        this.evaluateAndValueCheck(
                "=sumIf(123, 123)",
                EXPRESSION_NUMBER_KIND.create(123)
        );
    }

    @Test
    public void testEvaluateSumIfZero() {
        this.evaluateAndValueCheck(
                "=sumIf(123, 456)",
                EXPRESSION_NUMBER_KIND.zero()
        );
    }

    @Test
    public void testEvaluateSumIfSomeValuesFiltered() {
        this.evaluateAndValueCheck(
                "=sumIf(A2:A4, \">100\")",
                Maps.of(
                        "A2", "=1", //
                        "A3", "=200", //
                        "A4", "=\"400\"" // string with number converted
                ),
                EXPRESSION_NUMBER_KIND.create(600)
        );
    }

    @Test
    public void testEvaluateSwitchFirst() {
        this.evaluateAndValueCheck(
                "=switch(1, 1, \"One\", 2, \"Two\", 3, 333)",
                "One"
        );
    }

    @Test
    public void testEvaluateSwitchSecond() {
        this.evaluateAndValueCheck(
                "=switch(\"TWO22\", 1, \"One\", \"Two22\", \"Two\", 3, 333, \"switch-default\")",
                "Two"
        );
    }

    @Test
    public void testEvaluateSwitchDefaults() {
        this.evaluateAndValueCheck(
                "=switch(999, 1, \"One\", 22, \"Two\", 3, 333, \"switch-default\")",
                "switch-default"
        );
    }

    @Test
    public void testEvaluateTan() {
        this.evaluateAndValueCheck(
                "=tan(2)",
                EXPRESSION_NUMBER_KIND.create(-2.18504)
        );
    }

    @Test
    public void testEvaluateTanh() {
        this.evaluateAndValueCheck(
                "=tanh(2)",
                EXPRESSION_NUMBER_KIND.create(0.9640276)
        );
    }

    @Test
    public void testEvaluateTextWithDate() {
        this.evaluateAndValueCheck(
                "=text(date(1999,12,31), \"yyyy mm dd\")",
                "1999 12 31"
        );
    }

    @Test
    public void testEvaluateTextWithDateTime() {
        this.evaluateAndValueCheck(
                "=text(now(), \"yyyy mm dd hh mm ss\")",
                "1999 12 31 12 58 00"
        );
    }

    @Test
    public void testEvaluateTextWithNumber() {
        this.evaluateAndValueCheck(
                "=text(123.5, \"$0000.0000$\")",
                "$0123.5000$"
        );
    }

    @Test
    public void testEvaluateTextWithString() {
        this.evaluateAndValueCheck(
                "=text(\"abc\", \"Ignored-pattern\")",
                "abc"
        );
    }

    @Test
    public void testEvaluateTextWithTime() {
        this.evaluateAndValueCheck(
                "=text(time(12,58,59), \"ss hh mm\")",
                "59 12 58"
        );
    }

    @Test
    public void testEvaluateTextJoin() {
        this.evaluateAndFormattedCheck(
                "=textJoin(\",\", true(), \"a\", \"b\", \"\", \"d\")",
                TextNode.text("a,b,da,b,d")
        );
    }

    @Test
    public void testEvaluateTime() {
        this.evaluateAndValueCheck(
                "=time(12, 58, 59)",
                LocalTime.of(12, 58, 59)
        );
    }

    @Test
    public void testEvaluateTWithText() {
        this.evaluateAndValueCheck(
                "=t(\"abc123\")",
                "abc123"
        );
    }

    @Test
    public void testEvaluateToday() {
        this.evaluateAndValueCheck(
                "=today()",
                NOW.now()
                        .toLocalDate()
        );
    }

    @Test
    public void testEvaluateTrim() {
        this.evaluateAndValueCheck(
                "=trim(\"  a  b  c  \")",
                "a b c"
        );
    }

    @Test
    public void testEvaluateTrue() {
        this.evaluateAndValueCheck(
                "=true()",
                Boolean.TRUE
        );
    }

    @Test
    public void testEvaluateTrue2() {
        this.evaluateAndValueCheck(
                "=true()",
                Maps.of("A2", "=true()"),
                Boolean.TRUE
        );
    }

    @Test
    public void testEvaluateTrunc() {
        this.evaluateAndValueCheck(
                "=trunc(999.999,1)",
                EXPRESSION_NUMBER_KIND.create(999.9)
        );
    }

    @Test
    public void testEvaluateTruncWithNegativePlaces() {
        this.evaluateAndValueCheck(
                "=trunc(999.999,-2)",
                EXPRESSION_NUMBER_KIND.create(900)
        );
    }

    @Test
    public void testEvaluateTypeWithNumber() {
        this.evaluateAndValueCheck(
                "=type(123)",
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateTypeWithDate() {
        this.evaluateAndValueCheck(
                "=type(date(2000, 1, 1))",
                EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateTypeWithErrorDivByZero() {
        this.evaluateAndValueCheck(
                "=type(1/0)",
                EXPRESSION_NUMBER_KIND.create(16)
        );
    }

    @Test
    public void testEvaluateUnichar97() {
        this.evaluateAndValueCheck(
                "=unichar(97)",
                'a'
        );
    }

    @Test
    public void testEvaluateUnichar1000() {
        this.evaluateAndValueCheck(
                "=unichar(1000)",
                Character.valueOf((char) 1000)
        );
    }

    @Test
    public void testEvaluateUnicodeA() {
        this.evaluateAndValueCheck(
                "=unicode(\"A\")",
                EXPRESSION_NUMBER_KIND.create((int) 'A')
        );
    }

    @Test
    public void testEvaluateUnicodeChar1000() {
        final char c = 1000;
        this.evaluateAndValueCheck(
                "=unicode(\"" + c + "\")",
                EXPRESSION_NUMBER_KIND.create((int) c)
        );
    }

    @Test
    public void testEvaluateUpperWithNumber() {
        this.evaluateAndValueCheck(
                "=upper(1.25)",
                this.metadataWithStrangeNumberFormatPattern(),
                "1.25"
        );
    }


    @Test
    public void testEvaluateUpperWithString() {
        this.evaluateAndValueCheck(
                "=upper(\"ABCxyz\")",
                this.metadataWithStrangeNumberFormatPattern(),
                "ABCXYZ"
        );
    }

    @Test
    public void testEvaluateValueWithNumber() {
        this.evaluateAndValueCheck(
                "=value(123)",
                EXPRESSION_NUMBER_KIND.create(123)
        );
    }

    @Test
    public void testEvaluateValidationError() {
        this.evaluateAndValueCheck(
                "=ValidationError(\"#N/A Hello message 123\")",
                ValidationError.with(
                        SpreadsheetSelection.A1,
                        "Hello message 123"
                )
        );
    }

    @Test
    public void testEvaluateValueWithString() {
        this.evaluateAndValueCheck(
                "=value(\"123\")",
                EXPRESSION_NUMBER_KIND.create(123)
        );
    }

    @Test
    public void testEvaluateValueWithInvalidString() {
        this.evaluateAndValueCheck(
                "=value(\"abc\")",
                SpreadsheetErrorKind.VALUE.setMessage("Parameter \"number\": Invalid type walkingkooka.spreadsheet.SpreadsheetError expected walkingkooka.tree.expression.ExpressionNumber")
        );
    }

    @Test
    public void testEvaluateWeekday() {
        this.evaluateAndValueCheck(
                "=weekday(date(2022, 5, 12))",
                EXPRESSION_NUMBER_KIND.create(5)
        );
    }

    @Test
    public void testEvaluateWeeknum() {
        this.evaluateAndValueCheck(
                "=weeknum(date(2000, 2, 1))",
                EXPRESSION_NUMBER_KIND.create(6)
        );
    }

    @Test
    public void testEvaluateYear() {
        this.evaluateAndValueCheck(
                "=year(date(1999, 12, 31))",
                EXPRESSION_NUMBER_KIND.create(1999)
        );
    }

    @Test
    public void testEvaluateXorTrueTrueTrue() {
        this.evaluateAndValueCheck(
                "=xor(true(), true(), true())",
                Boolean.TRUE
        );
    }

    @Test
    public void testEvaluateXorFalseFalseTrue() {
        this.evaluateAndValueCheck(
                "=xor(true(), false(), true())",
                Boolean.FALSE
        );
    }

    @Test
    public void testEvaluateXorFalseFalseFalseFalse() {
        this.evaluateAndValueCheck(
                "=xor(false(), false(), false(), false())",
                Boolean.FALSE
        );
    }

    // evaluateAndCheckValue............................................................................................

    private SpreadsheetEngineContext evaluateAndValueCheck(final String cellFormula,
                                                           final Object expectedValue) {
        return this.evaluateAndValueCheck(
                cellFormula,
                Maps.empty(),
                expectedValue
        );
    }

    private SpreadsheetEngineContext evaluateAndValueCheck(final String cellFormula,
                                                           final SpreadsheetMetadata metadata,
                                                           final Object expectedValue) {
        return this.evaluateAndValueCheck(
                cellFormula,
                Maps.empty(),
                metadata,
                expectedValue
        );
    }

    private SpreadsheetEngineContext evaluateAndValueCheck(final String cellFormula,
                                                           final Map<String, String> preload,
                                                           final Object expectedValue) {
        return this.evaluateAndValueCheck(
                cellFormula,
                preload,
                this.metadata(),
                expectedValue
        );
    }

    private SpreadsheetEngineContext evaluateAndValueCheck(final String cellFormula,
                                                           final Map<String, String> preload,
                                                           final SpreadsheetMetadata metadata,
                                                           final Object expectedValue) {
        return this.evaluateAndCheck(
                SpreadsheetSelection.parseCell("A1"),
                cellFormula,
                preload,
                metadata,
                Optional.ofNullable(expectedValue),
                null // not checking formatted
        );
    }

    private void evaluateAndFormattedCheck(final String cellFormula,
                                           final TextNode expectedValue) {
        this.evaluateAndFormattedCheck(
                cellFormula,
                Maps.empty(),
                expectedValue
        );
    }

    private SpreadsheetEngineContext evaluateAndFormattedCheck(final String cellFormula,
                                                               final Map<String, String> preload,
                                                               final TextNode expectedFormatted) {
        return this.evaluateAndCheck(
                SpreadsheetSelection.parseCell("A1"),
                cellFormula,
                preload,
                null, // no value
                Optional.ofNullable(expectedFormatted)
        );
    }

    private SpreadsheetEngineContext evaluateAndCheck(final SpreadsheetCellReference cellReference,
                                                      final String cellFormula,
                                                      final Map<String, String> preload,
                                                      final Optional<?> expectedValue,
                                                      final Optional<TextNode> formatted) {
        return this.evaluateAndCheck(
                cellReference,
                cellFormula,
                preload,
                this.metadata(),
                expectedValue,
                formatted
        );
    }

    private SpreadsheetMetadata metadata() {
        return SpreadsheetMetadata.EMPTY
                .set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, SpreadsheetId.parse("1234"))
                .set(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME, SpreadsheetName.with("Untitled5678"))
                .set(SpreadsheetMetadataPropertyName.LOCALE, LOCALE)
                .loadFromLocale()
                .set(
                        SpreadsheetMetadataPropertyName.AUDIT_INFO,
                        AuditInfo.with(
                                EmailAddress.parse("creator@example.com"),
                                LocalDateTime.of(1999, 12, 31, 12, 58, 59),
                                EmailAddress.parse("modified@example.com"),
                                LocalDateTime.of(2000, 1, 2, 3, 4, 5)
                        )
                ).set(SpreadsheetMetadataPropertyName.CELL_CHARACTER_WIDTH, 1)
                .set(SpreadsheetMetadataPropertyName.DATETIME_OFFSET, Converters.EXCEL_1904_DATE_SYSTEM_OFFSET)
                .set(SpreadsheetMetadataPropertyName.DEFAULT_YEAR, 20)
                .set(SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND, EXPRESSION_NUMBER_KIND)
                .set(
                        SpreadsheetMetadataPropertyName.FORMULA_CONVERTER,
                        ConverterSelector.parse("collection (string-to-error, string-to-expression, string-to-selection, string-to-spreadsheet-metadata-property-name, string-to-spreadsheet-name, selection-to-selection, selection-to-string, error-to-number, error-throwing, general)")
                ).set(
                        SpreadsheetMetadataPropertyName.FORMULA_FUNCTIONS,
                        ExpressionFunctionAliasSet.parse(
                                EXPRESSION_FUNCTION_PROVIDER.expressionFunctionInfos()
                                        .stream()
                                        .map(i -> i.name().value())
                                        .collect(Collectors.joining(ExpressionFunctionAliasSet.SEPARATOR.string()))
                        )
                ).set(
                        SpreadsheetMetadataPropertyName.FORMAT_CONVERTER,
                        ConverterSelector.parse("collection (string-to-selection, selection-to-selection, selection-to-string, error-to-number, error-to-string, general)")
                ).set(SpreadsheetMetadataPropertyName.GENERAL_NUMBER_FORMAT_DIGIT_COUNT, SpreadsheetFormatterContext.DEFAULT_GENERAL_FORMAT_NUMBER_DIGIT_COUNT)
                .set(SpreadsheetMetadataPropertyName.PRECISION, MathContext.DECIMAL32.getPrecision())
                .set(SpreadsheetMetadataPropertyName.ROUNDING_MODE, RoundingMode.HALF_UP)
                .set(SpreadsheetMetadataPropertyName.NUMBER_FORMATTER, SpreadsheetPattern.parseNumberFormatPattern("#.###").spreadsheetFormatterSelector())
                .set(SpreadsheetMetadataPropertyName.TEXT_FORMATTER, SpreadsheetPattern.parseTextFormatPattern("@@").spreadsheetFormatterSelector())
                .set(SpreadsheetMetadataPropertyName.TWO_DIGIT_YEAR, 20)
                .set(
                        SpreadsheetMetadataPropertyName.STYLE,
                        TextStyle.EMPTY.set(TextStylePropertyName.WIDTH, Length.pixel(50.0))
                                .set(TextStylePropertyName.HEIGHT, Length.pixel(50.0))
                );
    }

    private SpreadsheetMetadata metadataWithStrangeNumberFormatPattern() {
        return this.metadata()
                .set(SpreadsheetMetadataPropertyName.NUMBER_FORMATTER, SpreadsheetPattern.parseNumberFormatPattern("\"Number:\"#.###").spreadsheetFormatterSelector());
    }

    private SpreadsheetEngineContext evaluateAndCheck(final SpreadsheetCellReference cellReference,
                                                      final String cellFormula,
                                                      final Map<String, String> preload,
                                                      final SpreadsheetMetadata metadata,
                                                      final Optional<?> expectedValue,
                                                      final Optional<TextNode> formatted) {
        final SpreadsheetEngine engine = SpreadsheetEngines.basic();

        final SpreadsheetMetadataStore metadataStore = SpreadsheetMetadataTesting.spreadsheetMetadataStore();
        metadataStore.save(metadata);

        final SpreadsheetStoreRepository repo = SpreadsheetStoreRepositories.basic(
                SpreadsheetCellStores.treeMap(),
                SpreadsheetCellReferencesStores.treeMap(),
                SpreadsheetColumnStores.treeMap(),
                SpreadsheetFormStores.treeMap(),
                SpreadsheetGroupStores.treeMap(),
                SpreadsheetLabelStores.treeMap(),
                SpreadsheetExpressionReferenceStores.treeMap(),
                metadataStore,
                SpreadsheetCellRangeStores.treeMap(),
                SpreadsheetCellRangeStores.treeMap(),
                SpreadsheetRowStores.treeMap(),
                StorageStores.tree(STORAGE_STORE_CONTEXT),
                SpreadsheetUserStores.treeMap()
        );

        final SpreadsheetEngineContext context = SpreadsheetEngineContexts.basic(
                SERVER_URL,
                metadata,
                repo,
                SpreadsheetMetadataPropertyName.FORMULA_FUNCTIONS,
                SpreadsheetProviders.basic(
                        SpreadsheetConvertersConverterProviders.spreadsheetConverters(
                                metadata,
                                SPREADSHEET_FORMATTER_PROVIDER,
                                SPREADSHEET_PARSER_PROVIDER
                        ),
                        EXPRESSION_FUNCTION_PROVIDER,
                        SPREADSHEET_COMPARATOR_PROVIDER,
                        SPREADSHEET_EXPORTER_PROVIDER,
                        SPREADSHEET_FORMATTER_PROVIDER,
                        SPREADSHEET_IMPORTER_PROVIDER,
                        SPREADSHEET_PARSER_PROVIDER,
                        VALIDATOR_PROVIDER
                ),
                PROVIDER_CONTEXT
        );

        // save all the preload cells, these will contain references in the test cell.
        for (final Map.Entry<String, String> referenceToExpression : preload.entrySet()) {
            final String reference = referenceToExpression.getKey();
            final String formula = referenceToExpression.getValue();

            engine.saveCell(
                    SpreadsheetSelection.parseCell(reference)
                            .setFormula(SpreadsheetFormula.EMPTY.setText(formula)),
                    context
            );
        }

        final SpreadsheetCell saved = engine.saveCell(
                        cellReference.setFormula(
                                SpreadsheetFormula.EMPTY.setText(cellFormula)
                        ),
                        context
                ).cell(cellReference)
                .orElseThrow(() -> new AssertionError("Missing " + cellReference + " after saving " + cellFormula));

        if (null != formatted) {
            this.checkEquals(
                    formatted,
                    saved.formattedValue(),
                    cellReference + "=" + cellFormula + "\n" +
                            preload.entrySet().stream()
                                    .map(e -> e.getKey() + "=" + e.getValue())
                                    .collect(Collectors.joining("\n"))
            );
        }

        if (null != expectedValue) {
            this.checkEquals(
                    expectedValue,
                    saved.formula()
                            .value(),
                    cellReference + "=" + cellFormula + "\n" +
                            preload.entrySet().stream()
                                    .map(e -> e.getKey() + "=" + e.getValue())
                                    .collect(Collectors.joining("\n"))
            );
        }

        return context;
    }

    // isPure..........................................................................................................

    // NOW()
    // TODAY()
    // RAND()
    // RANDBETWEEN()
    // OFFSET()
    // INDIRECT()
    // CELL() // depends on arguments
    // INFO() // depends on arguments
    @Test
    public void testIsPure() {
        final SpreadsheetExpressionEvaluationContext context = SpreadsheetExpressionEvaluationContexts.fake();

        final List<ExpressionFunction<?, ExpressionEvaluationContext>> pureFunctions = Lists.array();
        final ExpressionFunctionProvider provider = SpreadsheetExpressionFunctionProviders.expressionFunctionProvider(SpreadsheetExpressionFunctionNames.CASE_SENSITIVITY);

        provider.expressionFunctionInfos()
                .forEach(
                        i -> {
                            final boolean pure;

                            final ExpressionFunctionName name = i.name();

                            switch (name.value().toLowerCase()) {
                                case "now":
                                case "today":
                                case "rand":
                                case "randbetween":
                                case "offset":
                                case "cell":
                                case "info":
                                    pure = false;
                                    break;
                                default:
                                    pure = true;
                                    break;
                            }

                            final ExpressionFunction<?, ExpressionEvaluationContext> function = provider.expressionFunction(
                                    name,
                                    Lists.empty(),
                                    PROVIDER_CONTEXT
                            );
                            if (function.isPure(context) != pure) {
                                pureFunctions.add(function);
                            }
                        });

        this.checkEquals(
                Lists.empty(),
                pureFunctions,
                () -> "functions"
        );
    }

    // README...........................................................................................................

    // not really a test, basically a tool to help update the function list present in the README.
    @Test
    public void testReadmePrintFunctionList() {
        SpreadsheetExpressionFunctionProviders.expressionFunctionProvider(SpreadsheetExpressionFunctionNames.CASE_SENSITIVITY)
                .expressionFunctionInfos()
                .forEach(
                        i ->
                                System.out.println("  - " + i.name())
                );
    }

    // PublicStaticHelperTesting........................................................................................

    @Test
    public void testPublicStaticMethodsWithoutMathContextParameter() {
        this.publicStaticMethodParametersTypeCheck(MathContext.class);
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctions> type() {
        return SpreadsheetExpressionFunctions.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}

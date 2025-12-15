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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.color.RgbColorComponent;
import walkingkooka.color.WebColorName;
import walkingkooka.convert.Converters;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.environment.AuditInfo;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContextTesting;
import walkingkooka.environment.EnvironmentContexts;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.io.FakeTextReader;
import walkingkooka.io.TextReader;
import walkingkooka.io.TextReaders;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.HostAddress;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.net.http.server.HttpHandler;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.route.Router;
import walkingkooka.spreadsheet.SpreadsheetContext;
import walkingkooka.spreadsheet.SpreadsheetContexts;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorAliasSet;
import walkingkooka.spreadsheet.convert.provider.SpreadsheetConvertersConverterProviders;
import walkingkooka.spreadsheet.engine.SpreadsheetEngine;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineContext;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineContexts;
import walkingkooka.spreadsheet.engine.SpreadsheetEngines;
import walkingkooka.spreadsheet.engine.SpreadsheetMetadataMode;
import walkingkooka.spreadsheet.environment.SpreadsheetEnvironmentContext;
import walkingkooka.spreadsheet.environment.SpreadsheetEnvironmentContexts;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterAliasSet;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.expression.function.provider.SpreadsheetExpressionFunctionProviders;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterAliasSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterAliasSet;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.store.SpreadsheetMetadataStore;
import walkingkooka.spreadsheet.meta.store.SpreadsheetMetadataStores;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserAliasSet;
import walkingkooka.spreadsheet.provider.SpreadsheetProviders;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReferenceLoaders;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.store.repo.SpreadsheetStoreRepositories;
import walkingkooka.spreadsheet.store.repo.SpreadsheetStoreRepository;
import walkingkooka.spreadsheet.validation.form.SpreadsheetForms;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.spreadsheet.value.SpreadsheetError;
import walkingkooka.spreadsheet.value.SpreadsheetErrorKind;
import walkingkooka.terminal.TerminalContext;
import walkingkooka.terminal.TerminalContexts;
import walkingkooka.terminal.TerminalId;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.printer.Printers;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProvider;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.validation.ValidationChoice;
import walkingkooka.validation.ValidationChoiceList;
import walkingkooka.validation.form.provider.FormHandlerAliasSet;
import walkingkooka.validation.provider.ValidatorAliasSet;
import walkingkooka.validation.provider.ValidatorSelector;

import java.lang.reflect.Method;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class SpreadsheetExpressionFunctionsTest implements PublicStaticHelperTesting<SpreadsheetExpressionFunctions>,
    SpreadsheetMetadataTesting,
    TreePrintableTesting,
    EnvironmentContextTesting {

    private final static Locale LOCALE = Locale.forLanguageTag("EN-AU");
    private final static ExpressionFunctionProvider<SpreadsheetExpressionEvaluationContext> EXPRESSION_FUNCTION_PROVIDER = SpreadsheetExpressionFunctionProviders.expressionFunctionProvider(walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY);

    private final static Function<SpreadsheetContext, SpreadsheetEngineContext> SPREADSHEET_ENGINE_CONTEXT_FACTORY = (c) -> SpreadsheetEngineContexts.basic(
        SpreadsheetMetadataMode.FORMULA,
        c,
        TERMINAL_CONTEXT
    );

    private final static Function<SpreadsheetEngineContext, Router<HttpRequestAttribute<?>, HttpHandler>> HATEOS_ROUTER_FACTORY = (SpreadsheetEngineContext c) ->
        new Router<>() {
            @Override
            public Optional<HttpHandler> route(final Map<HttpRequestAttribute<?>, Object> parameters) {
                throw new UnsupportedOperationException();
            }
        };

    // wizard function names are all case-insensitive...................................................................

    @Test
    public void testCellFindWizardHelperFunctionConstants() {
        this.checkEquals(
            Sets.empty(),
            Arrays.stream(
                    SpreadsheetExpressionFunctions.class.getDeclaredFields()
                ).filter(m -> m.getType() == ExpressionFunctionName.class)
                .filter(m -> {
                        try {
                            return ExpressionFunctionName.class.cast(m.get(null))
                                .caseSensitivity() != walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY;
                        } catch (final Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                ).collect(Collectors.toSet())
        );
    }

    // all functions have case-insensitive names........................................................................

    @Test
    public void testFunctionsHaveCaseInsensitiveNames() {
        this.checkEquals(
            "",
            Arrays.stream(
                    SpreadsheetExpressionFunctions.class.getMethods()
                ).filter(m -> m.getReturnType() == ExpressionFunction.class)
                .filter(m -> {
                        try {
                            final ExpressionFunction<ExpressionFunctionName, ?> function = ExpressionFunction.class.cast(m.invoke(null));
                            final ExpressionFunctionName name = function.name()
                                .orElseThrow(() -> new IllegalStateException("Missing function name " + m.toGenericString()));

                            return name.caseSensitivity() != walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY;
                        } catch (final Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                ).map(Method::getName)
                .sorted()
                .collect(Collectors.joining("\n"))
        );
    }

    // error handling tests.............................................................................................

    @Test
    public void testEvaluateWithIncompleteExpression() {
        this.evaluateAndValueCheck(
            "=1+",
            SpreadsheetErrorKind.ERROR.setMessage(
                "End of text, expected LAMBDA_FUNCTION | NAMED_FUNCTION | \"TRUE\" | \"FALSE\" | LABEL | CELL_RANGE | CELL | GROUP | NEGATIVE | \"#.#E+#;#.#%;#.#;#%;#\" | TEXT | \"#NULL!\" | \"#DIV/0!\" | \"#VALUE!\" | \"#REF!\" | \"#NAME?\" | \"#NAME?\" | \"#NUM!\" | \"#N/A\" | \"#ERROR\" | \"#SPILL!\" | \"#CALC!\""
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
                walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.name("UnknownFunction123")
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
    public void testEvaluateAbsInvalidParameterTypeFails() {
        this.evaluateAndValueCheck(
            "=abs(\"Hello\")",
            SpreadsheetErrorKind.VALUE.setMessage("abs: number: Cannot convert \"Hello\" to ExpressionNumber")
        );
    }

    @Test
    public void testEvaluateAbs() {
        this.evaluateAndValueCheck(
            "=abs(-1.5)+abs(0.5)",
            EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testEvaluateAcosWithString() {
        this.evaluateAndValueCheck(
            "=acos(\"0.5\")",
            EXPRESSION_NUMBER_KIND.create(1.047198)
        );
    }

    @Test
    public void testEvaluateAcosWithNumber() {
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
    public void testEvaluateAndStringLiteralTrueStringLiteralTrue() {
        this.evaluateAndValueCheck(
            "=and(\"true\", \"true\")",
            Boolean.TRUE
        );
    }

    @Test
    public void testEvaluateAsinWithNumber() {
        this.evaluateAndValueCheck(
            "=asin(0.5)",
            EXPRESSION_NUMBER_KIND.create(0.5235988)
        );
    }

    @Test
    public void testEvaluateAsinWithString() {
        this.evaluateAndValueCheck(
            "=asin(\"0.5\")",
            EXPRESSION_NUMBER_KIND.create(0.5235988)
        );
    }

    @Test
    public void testEvaluateAtanWithNumber() {
        this.evaluateAndValueCheck(
            "=atan(0.5)",
            EXPRESSION_NUMBER_KIND.create(0.4636476)
        );
    }

    @Test
    public void testEvaluateAtanWithString() {
        this.evaluateAndValueCheck(
            "=atan(\"0.5\")",
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
    public void testEvaluateBadge() {
        this.evaluateAndValueCheck(
            "=badge(\"BadgeText111\", \"ChildTextNode222\")",
            TextNode.badge("BadgeText111")
                .appendChild(
                    TextNode.text("ChildTextNode222")
                )
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
    public void testEvaluateBitAndWithNumbers() {
        this.evaluateAndValueCheck(
            "=bitand(14, 7)",
            EXPRESSION_NUMBER_KIND.create(6)
        );
    }

    @Test
    public void testEvaluateBitAndWithString() {
        this.evaluateAndValueCheck(
            "=bitand(\"14\", \"7\")",
            EXPRESSION_NUMBER_KIND.create(6)
        );
    }

    @Test
    public void testEvaluateBitOrWithNumbers() {
        this.evaluateAndValueCheck(
            "=bitor(3, 6)",
            EXPRESSION_NUMBER_KIND.create(7)
        );
    }

    @Test
    public void testEvaluateBitOrWithStrings() {
        this.evaluateAndValueCheck(
            "=bitor(\"3\", \"6\")",
            EXPRESSION_NUMBER_KIND.create(7)
        );
    }

    @Test
    public void testEvaluateBitXorWithNumbers() {
        this.evaluateAndValueCheck(
            "=bitxor(7, 3)",
            EXPRESSION_NUMBER_KIND.create(4)
        );
    }

    @Test
    public void testEvaluateBitXorWithStrings() {
        this.evaluateAndValueCheck(
            "=bitxor(\"7\", \"3\")",
            EXPRESSION_NUMBER_KIND.create(4)
        );
    }

    @Test
    public void testEvaluateCeilWithNumber() {
        this.evaluateAndValueCheck(
            "=ceil(1.75)",
            EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testEvaluateCeilWithString() {
        this.evaluateAndValueCheck(
            "=ceil(\"1.75\")",
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
    public void testEvaluateCellDateTimeSymbols() {
        final DateTimeSymbols dateTimeSymbols = DateTimeSymbols.fromDateFormatSymbols(
            new DateFormatSymbols(Locale.ENGLISH)
        );
        this.evaluateAndValueCheck(
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText("=cellDateTimeSymbols()")
            ).setDateTimeSymbols(
                Optional.of(dateTimeSymbols)
            ),
            dateTimeSymbols
        );
    }

    @Test
    public void testEvaluateCellDecimalNumberSymbols() {
        final DecimalNumberSymbols decimalNumberSymbols = DecimalNumberSymbols.fromDecimalFormatSymbols(
            '+',
            new DecimalFormatSymbols(Locale.ENGLISH)
        );
        this.evaluateAndValueCheck(
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText("=cellDecimalNumberSymbols()")
            ).setDecimalNumberSymbols(
                Optional.of(decimalNumberSymbols)
            ),
            decimalNumberSymbols
        );
    }

    @Test
    public void testEvaluateCellFormatter() {
        final SpreadsheetFormatterSelector formatter = SpreadsheetFormatterSelector.parse("text @");
        this.evaluateAndValueCheck(
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText("=cellFormatter()")
            ).setFormatter(
                Optional.of(formatter)
            ),
            formatter
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
    public void testEvaluateCellValueType() {
        this.evaluateAndValueCheck(
            "=cellValueType()",
            Maps.of("A1", "=1*2*3*4"),
            null
        );
    }

    @Test
    public void testEvaluateCharWithNumber65() {
        this.evaluateAndValueCheck(
            "=char(65)",
            'A'
        );
    }

    @Test
    public void testEvaluateCharWithString65() {
        this.evaluateAndValueCheck(
            "=char(\"65\")",
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
    public void testEvaluateColorWithString() {
        this.evaluateAndValueCheck(
            "=color(\"#123456\")",
            Color.parse("#123456")
        );
    }

    @Test
    public void testEvaluateColorWithStringStringString() {
        this.evaluateAndValueCheck(
            "=color(\"1\",\"2\",\"3\")",
            Color.parse("#010203")
        );
    }

    @Test
    public void testEvaluateColorWithNumberNumberNumber() {
        this.evaluateAndValueCheck(
            "=color(1,2,3)",
            Color.parse("#010203")
        );
    }

    @Test
    public void testEvaluateColumnWithCell() {
        this.evaluateAndValueCheck(
            "=column(C1)",
            EXPRESSION_NUMBER_KIND.create(3)
        );
    }

    @Test
    public void testEvaluateColumnWithString() {
        this.evaluateAndValueCheck(
            "=column(\"C1\")",
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
    public void testEvaluateColumnsWithString() {
        this.evaluateAndValueCheck(
            "=columns(\"Z99\")",
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
    public void testEvaluateCosWithString() {
        this.evaluateAndValueCheck(
            "=cos(\"1\")",
            EXPRESSION_NUMBER_KIND.create(0.5403023)
        );
    }

    @Test
    public void testEvaluateCosWithNumber() {
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
    public void testEvaluateDec2BinWithNumber() {
        this.evaluateAndValueCheck(
            "=dec2bin(14)",
            "1110"
        );
    }

    @Test
    public void testEvaluateDec2BinWithString() {
        this.evaluateAndValueCheck(
            "=dec2bin(\"14\")",
            "1110"
        );
    }

    @Test
    public void testEvaluateDec2HexWithNumber() {
        this.evaluateAndValueCheck(
            "=dec2hex(255)",
            "ff"
        );
    }

    @Test
    public void testEvaluateDec2HexWithString() {
        this.evaluateAndValueCheck(
            "=dec2hex(\"255\")",
            "ff"
        );
    }

    @Test
    public void testEvaluateDec2OctWithString() {
        this.evaluateAndValueCheck(
            "=dec2oct(255)",
            "377"
        );
    }

    @Test
    public void testEvaluateDec2OctWithNumber() {
        this.evaluateAndValueCheck(
            "=dec2oct(255)",
            "377"
        );
    }

    @Test
    public void testEvaluateDegreesWithNumber() {
        this.evaluateAndValueCheck(
            "=degrees(1.5)",
            EXPRESSION_NUMBER_KIND.create(85.943655)
        );
    }

    @Test
    public void testEvaluateDegreesWithString() {
        this.evaluateAndValueCheck(
            "=degrees(\"1.5\")",
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
    public void testEvaluateEmailAddress() {
        this.evaluateAndValueCheck(
            "=emailAddress(\"user@example.com\")",
            EmailAddress.parse("user@example.com")
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
    public void testEvaluateExpWithNumber() {
        this.evaluateAndValueCheck(
            "=exp(1)",
            EXPRESSION_NUMBER_KIND.create(2.718282)
        );
    }

    @Test
    public void testEvaluateExpWithString() {
        this.evaluateAndValueCheck(
            "=exp(\"1\")",
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
    public void testEvaluateFixedWithString() {
        this.evaluateAndValueCheck(
            "=fixed(\"123.456\")",
            "123.46"
        );
    }

    @Test
    public void testEvaluateFloorWithNumber() {
        this.evaluateAndValueCheck(
            "=floor(1.8)",
            EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateFloorWithString() {
        this.evaluateAndValueCheck(
            "=floor(\"1.8\")",
            EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateFormatValue() {
        this.evaluateAndValueCheck(
            "=formatValue(\"text @@\", \"Hello\")",
            TextNode.text("HelloHello")
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
    public void testEvaluateGetAlpha() {
        this.evaluateAndValueCheck(
            "=getAlpha(color(\"#11223380\"))",
            Color.parseRgb("#11223380")
                .alpha()
        );
    }

    @Test
    public void testEvaluateGetBlue() {
        this.evaluateAndValueCheck(
            "=getBlue(color(\"#11223380\"))",
            Color.parseRgb("#11223380")
                .blue()
        );
    }

    @Test
    public void testEvaluateGetDateTimeSymbolsWithSpreadsheetCell() {
        final DateTimeSymbols dateTimeSymbols = DateTimeSymbols.fromDateFormatSymbols(
            DateFormatSymbols.getInstance(Locale.FRENCH)
        );

        final EnvironmentContext environmentContext = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        ).setEnvironmentValue(
            EnvironmentValueName.with("SpreadsheetCell"),
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY
            ).setDateTimeSymbols(
                Optional.of(dateTimeSymbols)
            )
        );

        this.evaluateAndPrintedCheck(
            "=getDateTimeSymbols(getEnv(\"SpreadsheetCell\"))",
            environmentContext,
            dateTimeSymbols,
            "" // printed
        );
    }

    @Test
    public void testEvaluateGetDateTimeSymbolsWithSpreadsheetCellMissingDateTimeSymbols() {
        final EnvironmentContext environmentContext = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        ).setEnvironmentValue(
            EnvironmentValueName.with("SpreadsheetCell"),
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY
            )
        );

        this.evaluateAndPrintedCheck(
            "=getDateTimeSymbols(getEnv(\"SpreadsheetCell\"))",
            environmentContext,
            null,
            "" // printed
        );
    }
    
    @Test
    public void testEvaluateGetDecimalNumberSymbolsWithSpreadsheetCell() {
        final DecimalNumberSymbols decimalNumberSymbols = DecimalNumberSymbols.fromDecimalFormatSymbols(
            '+',
            DecimalFormatSymbols.getInstance(Locale.FRENCH)
        );

        final EnvironmentContext environmentContext = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        ).setEnvironmentValue(
            EnvironmentValueName.with("SpreadsheetCell"),
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY
            ).setDecimalNumberSymbols(
                Optional.of(decimalNumberSymbols)
            )
        );

        this.evaluateAndPrintedCheck(
            "=getDecimalNumberSymbols(getEnv(\"SpreadsheetCell\"))",
            environmentContext,
            decimalNumberSymbols,
            "" // printed
        );
    }

    @Test
    public void testEvaluateGetEnvAndPrint() {
        final EnvironmentContext environmentContext = EnvironmentContexts.map(SPREADSHEET_ENVIRONMENT_CONTEXT);

        final String value = "Goodbye!";

        environmentContext.setEnvironmentValue(
            EnvironmentValueName.with("Hello"),
            value
        );

        this.evaluateAndPrintedCheck(
            "=print(getEnv(\"Hello\"))",
            environmentContext,
            value // printed
        );
    }

    @Test
    public void testEvaluateGetGreen() {
        this.evaluateAndValueCheck(
            "=getGreen(color(\"#11223380\"))",
            Color.parseRgb("#11223380")
                .green()
        );
    }

    @Test
    public void testEvaluateGetFormatterWithSpreadsheetCell() {
        final SpreadsheetFormatterSelector formatter = SpreadsheetFormatterSelector.parse("hello-formatter");

        final EnvironmentContext environmentContext = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        ).setEnvironmentValue(
            EnvironmentValueName.with("SpreadsheetCell"),
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY
            ).setFormatter(
                Optional.of(formatter)
            )
        );

        this.evaluateAndPrintedCheck(
            "=getFormatter(getEnv(\"SpreadsheetCell\"))",
            environmentContext,
            formatter,
            "" // printed
        );
    }

    @Test
    public void testEvaluateGetFormatValue() {
        this.evaluateAndValueCheck(
            "=getFormatValue()",
            SpreadsheetError.referenceNotFound(SpreadsheetExpressionEvaluationContext.FORMAT_VALUE)
        );
    }

    @Test
    public void testEvaluateGetFormulaTextWithSpreadsheetCell() {
        final String formulaText = "=1+2";

        final EnvironmentContext environmentContext = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        ).setEnvironmentValue(
            EnvironmentValueName.with("SpreadsheetCell"),
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText(formulaText)
            )
        );

        this.evaluateAndPrintedCheck(
            "=getFormulaText(getEnv(\"SpreadsheetCell\"))",
            environmentContext,
            formulaText,
            "" // printed
        );
    }

    @Test
    public void testEvaluateGetHostWithString() {
        this.evaluateAndValueCheck(
            "=getHost(\"https://example.com/path1\")",
            HostAddress.with("example.com")
        );
    }

    @Test
    public void testEvaluateGetLocaleWithStringWithLanguageTag() {
        this.evaluateAndValueCheck(
            "=getLocale(\"en-AU\")",
            Locale.forLanguageTag("en-AU")
        );
    }

    @Test
    public void testEvaluateGetLocaleWithSpreadsheetCell() {
        final Locale locale = Locale.FRENCH;

        final EnvironmentContext environmentContext = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        ).setEnvironmentValue(
            EnvironmentValueName.with("SpreadsheetCell"),
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY
            ).setLocale(
                Optional.of(locale)
            )
        );

        this.evaluateAndPrintedCheck(
            "=getLocale(getEnv(\"SpreadsheetCell\"))",
            environmentContext,
            locale,
            "" // printed
        );
    }

    @Test
    public void testEvaluateGetRed() {
        this.evaluateAndValueCheck(
            "=getRed(color(\"#11223380\"))",
            Color.parseRgb("#11223380")
                .red()
        );
    }

    @Test
    public void testEvaluateGetStyleWithString() {
        this.evaluateAndValueCheck(
            "=getStyle(\"color: #123456\")",
            TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR,
                Color.parse("#123456")
            )
        );
    }

    @Test
    public void testEvaluateGetStyleWithHyperlink() {
        this.evaluateAndValueCheck(
            "=getStyle(hyperlink(\"https://example.com\"))",
            TextStyle.EMPTY
        );
    }

    @Test
    public void testEvaluateGetStyleWithSetStyleWithHyperlink() {
        this.evaluateAndValueCheck(
            "=getStyle(setStyle(hyperlink(\"https://example.com\"),\"color: #123456\"))",
            TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR,
                Color.parse("#123456")
            )
        );
    }

    @Test
    public void testEvaluateGetStyleWithStyledText() {
        this.evaluateAndValueCheck(
            "=getStyle(styledText(\"Hello\",\"color: #123456\"))",
            TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR,
                Color.parse("#123456")
            )
        );
    }

    @Test
    public void testEvaluateGetTextNodeWithHyperlink() {
        this.evaluateAndValueCheck(
            "=getTextNode(hyperlink(\"https://example.com/123\"))",
            TextNode.hyperlink(Url.parseAbsolute("https://example.com/123"))
        );
    }

    @Test
    public void testEvaluateGetTextNodeWithString() {
        this.evaluateAndValueCheck(
            "=getTextNode(\"Hello123\")",
            TextNode.text("Hello123")
        );
    }

    @Test
    public void testEvaluateGetUser() {
        final EmailAddress user = EmailAddress.parse("user123@example.com");

        final EnvironmentContext environmentContext = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                Optional.of(user)
            )
        );

        this.evaluateAndPrintedCheck(
            "=getUser()",
            environmentContext,
            user,
            "" // printed
        );
    }

    @Test
    public void testEvaluateGetValidator() {
        final ValidatorSelector validator = ValidatorSelector.parse("hello-validator-123");

        final EnvironmentContext environmentContext = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        ).setEnvironmentValue(
            EnvironmentValueName.with("cell"),
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText("=1")
            ).setValidator(
                Optional.of(validator)
            )
        );

        this.evaluateAndPrintedCheck(
            "=getValidator(getEnv(\"cell\"))",
            environmentContext,
            validator,
            "" // printed
        );
    }

    @Test
    public void testEvaluateGetValue() {
        this.evaluateGetValueAndCheck("Hello World 123");
    }

    @Test
    public void testEvaluateGetValue2() {
        this.evaluateGetValueAndCheck(this);
    }

    @Test
    public void testEvaluateGetValueWhenAbsent() {
        this.evaluateGetValueAndCheck(null);
    }

    private void evaluateGetValueAndCheck(final Object value) {
        final EnvironmentContext environmentContext = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        ).setEnvironmentValue(
            EnvironmentValueName.with("cell"),
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText("=1")
                    .setValue(
                        Optional.ofNullable(value)
                    )
            )
        );

        this.evaluateAndPrintedCheck(
            "=getValue(getEnv(\"cell\"))",
            environmentContext,
            value,
            "" // printed
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
    public void testEvaluateIfFalseWithStringLiteralFalse() {
        this.evaluateAndValueCheck(
            "=if(\"false\", 111, 222)",
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

//    @Test
//    public void testEvaluateIfsFirstStringLiteralTrue() {
//        this.evaluateAndValueCheck(
//                "=ifs(\"true\", 111, false(), 222)",
//                EXPRESSION_NUMBER_KIND.create(111)
//        );
//    }

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
    public void testEvaluateIntWithNumber() {
        this.evaluateAndValueCheck(
            "=int(1.8)",
            EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateIntWithString() {
        this.evaluateAndValueCheck(
            "=int(\"1.8\")",
            EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateInvertColorWithColor() {
        this.evaluateAndValueCheck(
            "=invertColor(color(\"#123456\"))",
            Color.parse("#123456")
                .invert()
        );
    }

    @Test
    public void testEvaluateInvertColorWithString() {
        this.evaluateAndValueCheck(
            "=invertColor(\"#123456\")",
            Color.parse("#123456")
                .invert()
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

    // isDate only returns true when given a LocalDate or LocalDateTime

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
            false
        );
    }

    @Test
    public void testEvaluateIsDateWithString() {
        this.evaluateAndValueCheck(
            "=isDate(\"31/12/2000\")",
            false
        );
    }

    @Test
    public void testEvaluateIsDateWithTime() {
        this.evaluateAndValueCheck(
            "=isDate(time(1,1,1))",
            false
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
    public void testEvaluateIsEvenWithOddNumberStringLiteral() {
        this.evaluateAndValueCheck(
            "=isEven(\"1\")",
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
    public void testEvaluateListWithNoArguments() {
        this.evaluateAndPrintedCheck(
            "=list()",
            Lists.empty(),
            "" // expectedPrinted
        );
    }

    @Test
    public void testEvaluateList() {
        this.evaluateAndPrintedCheck(
            "=list(TRUE, 2, \"Cat\")",
            Lists.of(
                Boolean.TRUE,
                EXPRESSION_NUMBER_KIND.create(2),
                "Cat"
            ),
            "" // expectedPrinted
        );
    }

    @Test
    public void testEvaluateLnWithNumber() {
        this.evaluateAndValueCheck(
            "=ln(2)",
            EXPRESSION_NUMBER_KIND.create(0.6931472)
        );
    }

    @Test
    public void testEvaluateLnWithString() {
        this.evaluateAndValueCheck(
            "=ln(\"2\")",
            EXPRESSION_NUMBER_KIND.create(0.6931472)
        );
    }

    @Test
    public void testEvaluateLogWIthNumbers() {
        this.evaluateAndValueCheck(
            "=log(3, 2)",
            EXPRESSION_NUMBER_KIND.create(1.584962)
        );
    }

    @Test
    public void testEvaluateLogWithStrings() {
        this.evaluateAndValueCheck(
            "=log(\"3\", \"2\")",
            EXPRESSION_NUMBER_KIND.create(1.584962)
        );
    }

    @Test
    public void testEvaluateLog10WithNumber() {
        this.evaluateAndValueCheck(
            "=log10(100)",
            EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testEvaluateLog10WithStrings() {
        this.evaluateAndValueCheck(
            "=log10(\"100\")",
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
    public void testEvaluateMergeStyleWithStringAndString() {
        this.evaluateAndValueCheck(
            "=mergeStyle(\"background-color:#111;\",\"color:#222;\")",
            TextStyle.parse("background-color: #111; color: #222;")
        );
    }

    @Test
    public void testEvaluateMergeStyleWithTextNodeAndString() {
        this.evaluateAndValueCheck(
            "=mergeStyle(styledText(\"HelloText123\",\"background-color:#111;\"),\"color:#222;\")",
            TextNode.text("HelloText123")
                .setTextStyle(
                    TextStyle.parse("background-color: #111; color: #222;")
                )
        );
    }

    @Test
    public void testEvaluateMergeStyleAndStyledText() {
        this.evaluateAndValueCheck(
            "=styledText(\"HelloText123\", mergeStyle(\"color:#111;\",\"text-align:left\"))",
            TextNode.text("HelloText123")
                .setTextStyle(
                    TextStyle.parse("color: #111; text-align: left;")
                )
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
    public void testEvaluateMixColorWithColorColorNumber() {
        this.evaluateAndValueCheck(
            "=mixColor(color(\"#111\"), color(\"#222\"), 0.5)",
            Color.parse("#111")
                .mix(
                    Color.parse("#222"),
                    0.5f
                )
        );
    }

    @Test
    public void testEvaluateModWithNumbers() {
        this.evaluateAndValueCheck(
            "=mod(5, 3)",
            EXPRESSION_NUMBER_KIND.create(2)
        );
    }

    @Test
    public void testEvaluateModWithStrings() {
        this.evaluateAndValueCheck(
            "=mod(\"5\", \"3\")",
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
    public void testEvaluateNotZeroWithString() {
        this.evaluateAndValueCheck(
            "=not(\"0\")",
            Boolean.TRUE
        );
    }

    @Test
    public void testEvaluateNow() {
        this.evaluateAndValueCheck(
            "=now()",
            HAS_NOW.now()
        );
    }

    @Test
    public void testEvaluateNumberValue() {
        this.evaluateAndValueCheck(
            "=numberValue(\"1234.5\")",
            EXPRESSION_NUMBER_KIND.create(1234.5)
        );
    }

    @Test
    @Disabled // https://github.com/mP1/walkingkooka-spreadsheet-expression-function/issues/459
    public void testEvaluateNumberValueWithCustomDecimalSeparatorAndGroupSeparator() {
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
    public void testEvaluateOddWithNumber() {
        this.evaluateAndValueCheck(
            "=odd(12.3)",
            EXPRESSION_NUMBER_KIND.create(13)
        );
    }

    @Test
    public void testEvaluateOddWithString() {
        this.evaluateAndValueCheck(
            "=odd(\"12.3\")",
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
    public void testEvaluateOrFalseFalseFalseFalseStringLiterals() {
        this.evaluateAndValueCheck(
            "=or(\"false\", \"false\", \"false\", \"false\")",
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
    public void testEvaluatePrint() {
        this.evaluateAndPrintedCheck(
            "=print(\"Hello World\")",
            "Hello World" // no EOL!!!
        );
    }

    @Test
    public void testEvaluatePrintln() {
        this.evaluateAndPrintedCheck(
            "=println(\"Hello World\")",
            "Hello World\n"
        );
    }

    @Test
    public void testEvaluateProductWithNumbers() {
        this.evaluateAndValueCheck(
            "=product(2, 5)",
            EXPRESSION_NUMBER_KIND.create(10)
        );
    }

    @Test
    public void testEvaluateProductWithStrings() {
        this.evaluateAndValueCheck(
            "=product(\"2\", \"5\")",
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
    public void testEvaluateQuotientWithNumbers() {
        this.evaluateAndValueCheck(
            "=quotient(12, 3)",
            EXPRESSION_NUMBER_KIND.create(4)
        );
    }

    @Test
    public void testEvaluateQuotientWithStrings() {
        this.evaluateAndValueCheck(
            "=quotient(\"12\", \"3\")",
            EXPRESSION_NUMBER_KIND.create(4)
        );
    }

    @Test
    public void testEvaluateRadiansWithNumber() {
        this.evaluateAndValueCheck(
            "=radians(90)",
            EXPRESSION_NUMBER_KIND.create(1.5707961)
        );
    }

    @Test
    public void testEvaluateRadiansWithString() {
        this.evaluateAndValueCheck(
            "=radians(\"90\")",
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
    public void testEvaluateReadLine() {
        this.evaluateAndPrintedCheck(
            "=println(readLine())",
            new FakeTextReader() {
                @Override
                public Optional<String> readLine(final long timeout) {
                    return Optional.of("Hello World");
                }
            },
            "Hello World\n"
        );
    }

    @Test
    public void testEvaluateRemoveEnvAndPrint() {
        final EnvironmentContext environmentContext = EnvironmentContexts.map(SPREADSHEET_ENVIRONMENT_CONTEXT);

        final EnvironmentValueName<String> name = EnvironmentValueName.with("Hello");
        final String value = "Goodbye!";

        environmentContext.setEnvironmentValue(
            name,
            value
        );

        this.evaluateAndPrintedCheck(
            "=print(removeEnv(\"Hello\"))",
            environmentContext,
            value
        );

        // value deleted should be missing now
        this.environmentValueAndCheck(
            environmentContext,
            name
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
    public void testEvaluateReptWithStringNumberLiteral() {
        this.evaluateAndValueCheck(
            "=rept(\"abc\", \"3\")",
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
    public void testEvaluateRomanWithNumber() {
        this.evaluateAndValueCheck(
            "=roman(123)",
            "CXXIII"
        );
    }

    @Test
    public void testEvaluateRomanWithStringNumberLiteral() {
        this.evaluateAndValueCheck(
            "=roman(\"123\")",
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
    public void testEvaluateRoundWithStringNumberLiteral() {
        this.evaluateAndValueCheck(
            "=round(\"5.7845\", \"1\")",
            EXPRESSION_NUMBER_KIND.create(5.8)
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
    public void testEvaluateSetAlpha() {
        this.evaluateAndValueCheck(
            "=setAlpha(\"#112233\", \"64\")",
            Color.parseRgb("#112233")
                .set(
                    RgbColorComponent.alpha((byte) 64)
                )
        );
    }

    @Test
    public void testEvaluateSetBlue() {
        this.evaluateAndValueCheck(
            "=setBlue(\"#112233\", \"255\")",
            Color.parseRgb("#112233")
                .set(
                    RgbColorComponent.blue((byte) 255)
                )
        );
    }

    @Test
    public void testEvaluateSetEnvAndPrint() {
        final EnvironmentContext environmentContext = EnvironmentContexts.map(SPREADSHEET_ENVIRONMENT_CONTEXT);

        final EnvironmentValueName<String> name = EnvironmentValueName.with("Hello");
        final String value = "Goodbye!";

        environmentContext.setEnvironmentValue(
            name,
            value
        );

        this.evaluateAndPrintedCheck(
            "=print(setEnv(\"Hello\", \"Replacement2\"))",
            environmentContext,
            value
        );

        this.environmentValueAndCheck(
            environmentContext,
            name,
            "Replacement2"
        );
    }

    @Test
    public void testEvaluateSetGreen() {
        this.evaluateAndValueCheck(
            "=setGreen(\"#112233\", \"255\")",
            Color.parseRgb("#112233")
                .set(
                    RgbColorComponent.green((byte) 255)
                )
        );
    }

    @Test
    public void testEvaluateSetHostWithAbsoluteUrlAndString() {
        this.evaluateAndValueCheck(
            "=setHost(\"https://example.com/path1\", \"DIFFERENT.EXAMPLE.COM\")",
            Url.parseAbsolute("https://DIFFERENT.EXAMPLE.COM/path1")
        );
    }

    @Test
    public void testEvaluateSetHostWithEmailAddressAndString() {
        this.evaluateAndValueCheck(
            "=setHost(\"user@example.com\", \"DIFFERENT.EXAMPLE.COM\")",
            EmailAddress.parse("user@DIFFERENT.EXAMPLE.COM")
        );
    }

    @Test
    public void testEvaluateSetHostWithMailToUrlAndString() {
        this.evaluateAndValueCheck(
            "=setHost(\"mailto:user@example.com\", \"DIFFERENT.EXAMPLE.COM\")",
            Url.parseMailTo("mailto:user@DIFFERENT.EXAMPLE.COM")
        );
    }

    @Test
    public void testEvaluateSetLocaleAndPrint() {
        final EnvironmentContext environmentContext = EnvironmentContexts.map(SPREADSHEET_ENVIRONMENT_CONTEXT);

        final EnvironmentValueName<Locale> name = EnvironmentValueName.LOCALE;

        environmentContext.setEnvironmentValue(
            name,
            Locale.ENGLISH
        );

        this.evaluateAndPrintedCheck(
            "=print(setLocale(\"FR\"))",
            environmentContext,
            "null"
        );

        this.environmentValueAndCheck(
            environmentContext,
            name,
            Locale.forLanguageTag("FR")
        );
    }

    @Test
    public void testEvaluateSetRed() {
        this.evaluateAndValueCheck(
            "=setRed(\"#123\", \"9\")",
            Color.parseRgb("#123")
                .set(
                    RgbColorComponent.red((byte) 9)
                )
        );
    }

    @Test
    public void testEvaluateSetStyle() {
        this.evaluateAndValueCheck(
            "=setStyle(hyperlink(\"https://example.com\"),\"color: #123456\")",
            TextNode.hyperlink(
                Url.parseAbsolute("https://example.com")
            ).setTextStyle(
                TextStyle.EMPTY.set(
                    TextStylePropertyName.COLOR,
                    Color.parse("#123456")
                )
            )
        );
    }

    @Test
    public void testEvaluateSetTextWithHyperlink() {
        this.evaluateAndValueCheck(
            "=setText(hyperlink(\"https://example.com\"),\"Text123\")",
            TextNode.hyperlink(
                Url.parseAbsolute("https://example.com")
            ).setText("Text123")
        );
    }

    @Test
    public void testEvaluateSetTextWithStringAndString() {
        this.evaluateAndValueCheck(
            "=setText(\"LostText111\",\"Text222\")",
            TextNode.text(
                "LostText111"
            ).setText("Text222")
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
    public void testEvaluateSignWithPositiveNumberStringLiteral() {
        this.evaluateAndValueCheck(
            "=sign(\"+123\")",
            EXPRESSION_NUMBER_KIND.one()
        );
    }

    @Test
    public void testEvaluateSinWithNumber() {
        this.evaluateAndValueCheck(
            "=sin(1)",
            EXPRESSION_NUMBER_KIND.create(0.841471)
        );
    }

    @Test
    public void testEvaluateSinWithString() {
        this.evaluateAndValueCheck(
            "=sin(\"1\")",
            EXPRESSION_NUMBER_KIND.create(0.841471)
        );
    }

    @Test
    public void testEvaluateSinhWithNumber() {
        this.evaluateAndValueCheck(
            "=sinh(1)",
            EXPRESSION_NUMBER_KIND.create(1.175201)
        );
    }

    @Test
    public void testEvaluateSinhWithString() {
        this.evaluateAndValueCheck(
            "=sinh(\"1\")",
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
    public void testEvaluateSqrtWithStringPositiveNumber() {
        this.evaluateAndValueCheck(
            "=sqrt(\"100\")",
            EXPRESSION_NUMBER_KIND.create(10)
        );
    }

    @Test
    public void testEvaluateStyle() {
        this.evaluateAndValueCheck(
            "=style(\"text-align: left;\")",
            TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
            )
        );
    }

    @Test
    public void testEvaluateStyleGet() {
        this.evaluateAndValueCheck(
            "=styleGet(hyperlink(\"https://example.com\"), \"color\")",
            TextNode.hyperlink(
                    Url.parseAbsolute("https://example.com")
                ).textStyle()
                .get(TextStylePropertyName.COLOR)
                .orElse(null)
        );
    }

    @Test
    public void testEvaluateStyleGet2() {
        this.evaluateAndValueCheck(
            "=styleGet(styleSet(hyperlink(\"https://example.com\"),\"color\",\"#123456\"),\"color\")",
            TextNode.hyperlink(
                    Url.parseAbsolute("https://example.com")
                ).setTextStyle(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.COLOR,
                        Color.parse("#123456")
                    )
                ).textStyle()
                .get(TextStylePropertyName.COLOR)
                .orElse(null)
        );
    }

    @Test
    public void testEvaluateStyleRemoveWithTextStyleAndString() {
        this.evaluateAndValueCheck(
            "=styleRemove(\"background-color: #111111; color: #222222\",\"color\")",
            TextStyle.EMPTY.set(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.parse("#111111")
            )
        );
    }

    @Test
    public void testEvaluateStyleRemoveWithTextNodeAndString() {
        this.evaluateAndValueCheck(
            "=styleRemove(styledText(\"HelloText\",\"background-color: #111111; color: #222222\"),\"color\")",
            TextNode.text("HelloText")
                .setTextStyle(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.BACKGROUND_COLOR,
                        Color.parse("#111111")
                    )
                )
        );
    }

    @Test
    public void testEvaluateStyleSetWithStringAndStringAndString() {
        this.evaluateAndValueCheck(
            "=styleSet(\"background-color: #111111\",\"color\",\"#222222\")",
            TextStyle.EMPTY.set(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.parse("#111111")
            ).set(
                TextStylePropertyName.COLOR,
                Color.parse("#222222")
            )
        );
    }

    @Test
    public void testEvaluateStyleSetWithTextStyleAndStringAndString() {
        this.evaluateAndValueCheck(
            "=styleSet(style(\"background-color: #111111\"),\"color\",\"#222222\")",
            TextStyle.EMPTY.set(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.parse("#111111")
            ).set(
                TextStylePropertyName.COLOR,
                Color.parse("#222222")
            )
        );
    }

    @Test
    public void testEvaluateStyleSetWithTextNodeAndStringAndString() {
        this.evaluateAndValueCheck(
            "=styleSet(styledText(\"HelloText123\",\"background-color: #111111\"),\"color\",\"#222222\")",
            TextNode.text("HelloText123")
                .setTextStyle(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.BACKGROUND_COLOR,
                        Color.parse("#111111")
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.parse("#222222")
                    )
                )
        );
    }

    @Test
    public void testEvaluateStyleSetWithTextNodeAndStringAndString2() {
        this.evaluateAndValueCheck(
            "=styleSet(styledText(\"HelloText123\",style(\"background-color: #111111\")),\"color\",\"#222222\")",
            TextNode.text("HelloText123")
                .setTextStyle(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.BACKGROUND_COLOR,
                        Color.parse("#111111")
                    ).set(
                        TextStylePropertyName.COLOR,
                        Color.parse("#222222")
                    )
                )
        );
    }

    @Test
    public void testEvaluateStyledText() {
        this.evaluateAndValueCheck(
            "=styledText(\"Text123\",\"{color:#123456\")",
            TextNode.text("Text123")
                .setTextStyle(
                    TextStyle.parse("{color:#123456}")
                )
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
    public void testEvaluateSwitchFirstString() {
        this.evaluateAndValueCheck(
            "=switch(\"1\", \"1\", \"One\", 2, \"Two\", 3, 333)",
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
    public void testEvaluateTanWithNumber() {
        this.evaluateAndValueCheck(
            "=tan(2)",
            EXPRESSION_NUMBER_KIND.create(-2.18504)
        );
    }

    @Test
    public void testEvaluateTanWithString() {
        this.evaluateAndValueCheck(
            "=tan(\"2\")",
            EXPRESSION_NUMBER_KIND.create(-2.18504)
        );
    }

    @Test
    public void testEvaluateTanhWithNumber() {
        this.evaluateAndValueCheck(
            "=tanh(2)",
            EXPRESSION_NUMBER_KIND.create(0.9640276)
        );
    }

    @Test
    public void testEvaluateTanhWithString() {
        this.evaluateAndValueCheck(
            "=tanh(\"2\")",
            EXPRESSION_NUMBER_KIND.create(0.9640276)
        );
    }

    @Test
    public void testEvaluateTemplateWithPlainTextString() {
        this.evaluateAndValueCheck(
            "=template(\"Hello World 123\")",
            "Hello World 123"
        );
    }

    @Test
    public void testEvaluateTemplateWithNamedParameter() {
        this.evaluateAndValueCheck(
            "=template(\"Hello ${hello} !!!\", \"hello\", \"WORLD\")",
            "Hello WORLD !!!"
        );
    }

    @Test
    public void testEvaluateTemplateWithNamedParameter2() {
        this.evaluateAndValueCheck(
            "=template(\"Hello ${hello1} and ${hello2} !!!\", \"hello1\", \"WORLD\", \"hello2\", \"WORLD2\")",
            "Hello WORLD and WORLD2 !!!"
        );
    }

    @Test
    public void testEvaluateTemplateWithExpression() {
        this.evaluateAndValueCheck(
            "=template(\"Before ${sum(1,2,3)} After\")",
            "Before 6 After"
        );
    }

    @Test
    public void testEvaluateTemplateWithUnknownLabel() {
        this.evaluateAndValueCheck(
            "=template(\"${Hello} 123\")",
            "#NAME? 123"
        );
    }

    @Test
    public void testEvaluateTemplateWithLabel() {
        final SpreadsheetEngine engine = SpreadsheetEngines.basic();

        final SpreadsheetMetadata metadata = this.metadata()
            .set(
                SpreadsheetMetadataPropertyName.TEXT_FORMATTER,
                SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN.spreadsheetFormatterSelector()
            );

        final SpreadsheetMetadataStore metadataStore = SpreadsheetMetadataStores.treeMap();
        final SpreadsheetMetadata saved = metadataStore.save(metadata);

        final SpreadsheetStoreRepository repo = SpreadsheetStoreRepositories.treeMap(metadataStore);

        final SpreadsheetEngineContext context = SpreadsheetEngineContexts.basic(
            SpreadsheetMetadataMode.FORMULA,
            SpreadsheetContexts.basic(
                (id) -> repo,
                SpreadsheetProviders.basic(
                    SpreadsheetConvertersConverterProviders.spreadsheetConverters(
                        (ProviderContext p) -> metadata.dateTimeConverter(
                            SPREADSHEET_FORMATTER_PROVIDER,
                            SPREADSHEET_PARSER_PROVIDER,
                            p
                        )
                    ),
                    EXPRESSION_FUNCTION_PROVIDER,
                    SPREADSHEET_COMPARATOR_PROVIDER,
                    SPREADSHEET_EXPORTER_PROVIDER,
                    SPREADSHEET_FORMATTER_PROVIDER,
                    FORM_HANDLER_PROVIDER,
                    SPREADSHEET_IMPORTER_PROVIDER,
                    SPREADSHEET_PARSER_PROVIDER,
                    VALIDATOR_PROVIDER
                ),
                SPREADSHEET_ENGINE_CONTEXT_FACTORY,
                HATEOS_ROUTER_FACTORY,
                SpreadsheetEnvironmentContexts.basic(
                    EnvironmentContexts.map(SPREADSHEET_ENVIRONMENT_CONTEXT)
                        .setEnvironmentValue(
                            SpreadsheetEnvironmentContext.SPREADSHEET_ID,
                            saved.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID)
                        )
                ),
                LocaleContexts.jre(LOCALE),
                PROVIDER_CONTEXT,
                TERMINAL_SERVER_CONTEXT
            ),
            TERMINAL_CONTEXT
        );

        final SpreadsheetCellReference labelTarget = SpreadsheetSelection.parseCell("B2");

        engine.saveLabel(
            SpreadsheetSelection.labelName("Hello")
                .setLabelMappingReference(labelTarget),
            context
        );

        engine.saveCell(
            labelTarget.setFormula(
                SpreadsheetFormula.EMPTY.setValue(
                    Optional.of("WORLD")
                )
            ),
            context
        );

        final SpreadsheetCell cell = SpreadsheetSelection.A1.setFormula(
            SpreadsheetFormula.EMPTY.setText("=template(\"${Hello} 123\")")
        );

        final SpreadsheetCell savedCell = engine.saveCell(
                cell,
                context
            ).cell(cell.reference())
            .get();

        this.checkEquals(
            TextNode.text("WORLD 123"),
            savedCell.formattedValue()
                .orElse(null)
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
    public void testEvaluateTextMatchWithStringLiteral() {
        this.evaluateAndValueCheck(
            "=textMatch(\"*e*\", \"Hello\")",
            true
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
            HAS_NOW.now()
                .toLocalDate()
        );
    }

    @Test
    public void testEvaluateToGrayWithColor() {
        this.evaluateAndValueCheck(
            "=toGray(color(\"#123\"))",
            Color.parse("#123")
                .toRgb()
                .toGray()
        );
    }

    @Test
    public void testEvaluateToGrayWithStringRgb() {
        this.evaluateAndValueCheck(
            "=toGray(\"#123\")",
            Color.parse("#123")
                .toRgb()
                .toGray()
        );
    }

    @Test
    public void testEvaluateToHslColorWithColor() {
        this.evaluateAndValueCheck(
            "=toHslColor(color(\"#123456\"))",
            Color.parse("#123456")
                .toHsl()
        );
    }

    @Test
    public void testEvaluateToHslColorWithString() {
        this.evaluateAndValueCheck(
            "=toHslColor(\"#123456\")",
            Color.parse("#123456")
                .toHsl()
        );
    }

    @Test
    public void testEvaluateToHsvColorWithColor() {
        this.evaluateAndValueCheck(
            "=toHsvColor(color(\"#123456\"))",
            Color.parse("#123456")
                .toHsv()
        );
    }

    @Test
    public void testEvaluateToHsvColorWithString() {
        this.evaluateAndValueCheck(
            "=toHsvColor(\"#123456\")",
            Color.parse("#123456")
                .toHsv()
        );
    }
    
    @Test
    public void testEvaluateToRgbColorWithColor() {
        this.evaluateAndValueCheck(
            "=toRgbColor(color(\"#123456\"))",
            Color.parse("#123456")
                .toRgb()
        );
    }

    @Test
    public void testEvaluateToRgbColorWithString() {
        this.evaluateAndValueCheck(
            "=toRgbColor(\"#123456\")",
            Color.parse("#123456")
                .toRgb()
        );
    }
    
    @Test
    public void testEvaluateToRgbHexStringWithColor() {
        this.evaluateAndValueCheck(
            "=toRgbHexString(color(\"#123456\"))",
            Color.parse("#123456")
                .toRgb()
                .toHexString()
        );
    }

    @Test
    public void testEvaluateToRgbHexStringWithString() {
        this.evaluateAndValueCheck(
            "=toRgbHexString(\"#123456\")",
            Color.parse("#123456")
                .toRgb()
                .toHexString()
        );
    }

    @Test
    public void testEvaluateToWebColorNameWithColor() {
        this.evaluateAndValueCheck(
            "=toWebColorName(color(\"blue\"))",
            WebColorName.BLUE
        );
    }

    @Test
    public void testEvaluateToWebColorNameWithString() {
        this.evaluateAndValueCheck(
            "=toWebColorName(\"blue\")",
            WebColorName.BLUE
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
    public void testEvaluateTruncWithNegativePlacesStrings() {
        this.evaluateAndValueCheck(
            "=trunc(\"999.999\",\"-2\")",
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
    public void testEvaluateUrlWithStringAbsoluteUrl() {
        this.evaluateAndValueCheck(
            "=url(\"https://example.com/path1\")",
            Url.parseAbsolute("https://example.com/path1")
        );
    }

    @Test
    public void testEvaluateUrlWithStringDataUrl() {
        this.evaluateAndValueCheck(
            "=url(\"data:,Hello%2C%20World%21\")",
            Url.parseData("data:,Hello%2C%20World%21")
        );
    }

    @Test
    public void testEvaluateUrlWithStringRelativeUrl() {
        this.evaluateAndValueCheck(
            "=url(\"/path1?k1=v1#fragment111\")",
            Url.parseRelative("/path1?k1=v1#fragment111")
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
    public void testEvaluateValidationChoiceListWithListOfString() {
        // SpreadsheetCell#SpreadsheetFormula will wrap any Collection with a SpreadsheetError#VALUE
        this.evaluateAndValueCheck(
            "=ValidationChoiceList(list(\"Label1\"))",
            SpreadsheetErrorKind.VALUE.toError()
                .setValue(
                    Optional.of(
                        ValidationChoiceList.EMPTY.concat(
                            ValidationChoice.with(
                                "Label1",
                                Optional.of("Label1")
                            )
                        )
                    )
                )
        );
    }

    @Test
    public void testEvaluateValidationChoiceListWithCsvStringFails() {
        // SpreadsheetCell#SpreadsheetFormula will wrap any Collection with a SpreadsheetError#VALUE
        this.evaluateAndValueCheck(
            "=ValidationChoiceList(\"Label1,Label2,Label3\")",
            SpreadsheetErrorKind.VALUE.toError()
                .setValue(
                    Optional.of(
                        "validationChoiceList: values: Cannot convert \"Label1,Label2,Label3\" to List"
                    )
                )
        );
    }

    @Test
    public void testEvaluateValidationError() {
        this.evaluateAndValueCheck(
            "=ValidationError(\"#N/A Hello message 123\")",
            SpreadsheetForms.error(SpreadsheetSelection.A1)
                .setMessage("Hello message 123")
        );
    }

    // Converting String  "#N/A Hello message 456" will fail because the Context#validationReference will be absent
    // because MODE=FORMULA and not MODE=VALIDATION
    @Test
    public void testEvaluateValidationErrorIfTrueAndString() {
        this.evaluateAndValueCheck(
            "=ValidationErrorIf(true(), \"#N/A Hello message 456\")",
            SpreadsheetErrorKind.VALUE.setMessage("validationErrorIf: validationError: Cannot convert \"#N/A Hello message 456\" to ValidationError")
        );
    }

    @Test
    public void testEvaluateValidationErrorIfTrueAndValidationError() {
        this.evaluateAndValueCheck(
            "=ValidationErrorIf(true(), ValidationError(\"#N/A Hello message 123\"))",
            SpreadsheetForms.error(SpreadsheetSelection.A1)
                .setMessage("Hello message 123")
        );
    }

    @Test
    public void testEvaluateValidationValue() {
        this.evaluateAndValueCheck(
            "=ValidationValue()",
            SpreadsheetError.selectionNotFound(
                SpreadsheetSelection.labelName("value")
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
            SpreadsheetErrorKind.VALUE.setMessage("value: number: Cannot convert \"abc\" to ExpressionNumber")
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

    @Test
    public void testEvaluateXorFalseFalseFalseFalseStringLiterals() {
        this.evaluateAndValueCheck(
            "=xor(\"false\", \"false\", \"false\", \"false\")",
            Boolean.FALSE
        );
    }

    @Test
    public void testEvaluateXorTrueFalseStringLiterals() {
        this.evaluateAndValueCheck(
            "=xor(\"true\", \"false\")",
            Boolean.TRUE
        );
    }

    // evaluateAndCheckValue............................................................................................

    private SpreadsheetEngineContext evaluateAndPrintedCheck(final String formula,
                                                             final String expectedPrinted) {
        return this.evaluateAndPrintedCheck(
            formula,
            (Object) null, // NO EXPECTED VALUE
            expectedPrinted
        );
    }

    private SpreadsheetEngineContext evaluateAndPrintedCheck(final String formula,
                                                             final Object expectedValue,
                                                             final String expectedPrinted) {
        return this.evaluateAndPrintedCheck(
            formula,
            EnvironmentContexts.map(SPREADSHEET_ENVIRONMENT_CONTEXT),
            expectedValue,
            expectedPrinted
        );
    }

    private SpreadsheetEngineContext evaluateAndPrintedCheck(final String formula,
                                                             final TextReader terminalInput,
                                                             final String expectedPrinted) {
        return this.evaluateAndPrintedCheck(
            formula,
            terminalInput,
            null, // NO EXPECTED VALUE
            expectedPrinted
        );
    }

    private SpreadsheetEngineContext evaluateAndPrintedCheck(final String formula,
                                                             final TextReader terminalInput,
                                                             final Object expectedValue,
                                                             final String expectedPrinted) {
        return this.evaluateAndPrintedCheck(
            formula,
            terminalInput,
            EnvironmentContexts.map(SPREADSHEET_ENVIRONMENT_CONTEXT),
            expectedValue,
            expectedPrinted
        );
    }

    private SpreadsheetEngineContext evaluateAndPrintedCheck(final String formula,
                                                             final EnvironmentContext environmentContext,
                                                             final String expectedPrinted) {
        return this.evaluateAndPrintedCheck(
            formula,
            environmentContext,
            (Object) null, // no expected value
            expectedPrinted
        );
    }

    private SpreadsheetEngineContext evaluateAndPrintedCheck(final String formula,
                                                             final EnvironmentContext environmentContext,
                                                             final Object expectedValue,
                                                             final String expectedPrinted) {
        return this.evaluateAndPrintedCheck(
            formula,
            TextReaders.fake(),
            environmentContext,
            expectedValue,
            expectedPrinted
        );
    }

    private SpreadsheetEngineContext evaluateAndPrintedCheck(final String formula,
                                                             final TextReader terminalInput,
                                                             final EnvironmentContext environmentContext,
                                                             final Object expectedValue,
                                                             final String expectedPrint) {
        final StringBuilder printed = new StringBuilder();

        final SpreadsheetMetadata metadata = this.metadata();

        final SpreadsheetEngineContext spreadsheetEngineContext = this.spreadsheetEngineContext(
            SpreadsheetMetadataMode.SCRIPTING,
            metadata.set(
                SpreadsheetMetadataPropertyName.SCRIPTING_CONVERTER,
                metadata.getOrFail(SpreadsheetMetadataPropertyName.FORMULA_CONVERTER)
            ).set(
                SpreadsheetMetadataPropertyName.SCRIPTING_FUNCTIONS,
                metadata.getOrFail(SpreadsheetMetadataPropertyName.FORMULA_FUNCTIONS)
            ),
            environmentContext,
            TerminalContexts.basic(
                TerminalId.with(1),
                environmentContext, // HasUser
                terminalInput,
                Printers.stringBuilder(
                    printed,
                    LineEnding.NL
                ),
                Printers.fake(),
                (t) -> {
                    throw new UnsupportedOperationException();
                }
            ),
            ProviderContexts.fake()
        );

        // only SCRIPTING allows updatable EnvironmentContext
        final SpreadsheetExpressionEvaluationContext spreadsheetExpressionEvaluationContext = spreadsheetEngineContext.setSpreadsheetMetadataMode(SpreadsheetMetadataMode.SCRIPTING)
            .spreadsheetExpressionEvaluationContext(
                SpreadsheetExpressionEvaluationContext.NO_CELL, // no cell
                SpreadsheetExpressionReferenceLoaders.spreadsheetStoreRepository(
                    spreadsheetEngineContext.storeRepository()
                )
            );

        final Object value = spreadsheetExpressionEvaluationContext.evaluateExpression(
            spreadsheetEngineContext.parseFormula(
                    TextCursors.charSequence(formula),
                    SpreadsheetExpressionEvaluationContext.NO_CELL
                ).toExpression(spreadsheetExpressionEvaluationContext)
                .orElseThrow(() -> new IllegalStateException("Unable to make Expression"))
        );

        if (null != expectedValue) {
            this.checkEquals(
                expectedValue,
                value,
                () -> "Evaluated " + CharSequences.quoteAndEscape(formula) + " returned value"
            );
        }

        if (null != printed) {
            this.checkEquals(
                expectedPrint,
                printed.toString(),
                () -> "Evaluated " + CharSequences.quoteAndEscape(formula) + " printed output"
            );
        }

        return spreadsheetEngineContext;
    }

    private SpreadsheetEngineContext evaluateAndValueCheck(final String formula,
                                                           final Object expectedValue) {
        return this.evaluateAndValueCheck(
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText(formula)
            ),
            expectedValue
        );
    }

    private SpreadsheetEngineContext evaluateAndValueCheck(final SpreadsheetCell save,
                                                           final Object expectedValue) {
        return this.evaluateAndValueCheck(
            save,
            Maps.empty(),
            expectedValue
        );
    }

    private SpreadsheetEngineContext evaluateAndValueCheck(final String formula,
                                                           final SpreadsheetMetadata metadata,
                                                           final Object expectedValue) {
        return this.evaluateAndValueCheck(
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText(formula)
            ),
            metadata,
            expectedValue
        );
    }

    private SpreadsheetEngineContext evaluateAndValueCheck(final SpreadsheetCell save,
                                                           final SpreadsheetMetadata metadata,
                                                           final Object expectedValue) {
        return this.evaluateAndValueCheck(
            save,
            Maps.empty(),
            metadata,
            expectedValue
        );
    }

    private SpreadsheetEngineContext evaluateAndValueCheck(final String formula,
                                                           final Map<String, String> preload,
                                                           final Object expectedValue) {
        return this.evaluateAndValueCheck(
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText(formula)
            ),
            preload,
            this.metadata(),
            expectedValue
        );
    }

    private SpreadsheetEngineContext evaluateAndValueCheck(final SpreadsheetCell save,
                                                           final Map<String, String> preload,
                                                           final Object expectedValue) {
        return this.evaluateAndValueCheck(
            save,
            preload,
            this.metadata(),
            expectedValue
        );
    }

    private SpreadsheetEngineContext evaluateAndValueCheck(final String formula,
                                                           final Map<String, String> preload,
                                                           final SpreadsheetMetadata metadata,
                                                           final Object expectedValue) {
        return this.evaluateAndValueCheck(
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText(formula)
            ),
            preload,
            metadata,
            expectedValue
        );
    }

    private SpreadsheetEngineContext evaluateAndValueCheck(final SpreadsheetCell save,
                                                           final Map<String, String> preload,
                                                           final SpreadsheetMetadata metadata,
                                                           final Object expectedValue) {
        return this.evaluateAndCheck(
            save,
            preload,
            metadata,
            Optional.ofNullable(expectedValue),
            null // not checking formatted
        );
    }

    private void evaluateAndFormattedCheck(final String formula,
                                           final TextNode expectedValue) {
        this.evaluateAndFormattedCheck(
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText(formula)
            ),
            Maps.empty(),
            expectedValue
        );
    }

    private SpreadsheetEngineContext evaluateAndFormattedCheck(final SpreadsheetCell save,
                                                               final Map<String, String> preload,
                                                               final TextNode expectedFormatted) {
        return this.evaluateAndCheck(
            save,
            preload,
            null, // no value
            Optional.ofNullable(expectedFormatted)
        );
    }

    private SpreadsheetEngineContext evaluateAndCheck(final SpreadsheetCell save,
                                                      final Map<String, String> preload,
                                                      final Optional<?> expectedValue,
                                                      final Optional<TextNode> formatted) {
        return this.evaluateAndCheck(
            save,
            preload,
            this.metadata(),
            expectedValue,
            formatted
        );
    }

    // FORMULA_CONVERTER added "form-and-validation" allowing some validation functions to be better tested.
    private SpreadsheetMetadata metadata() {
        final ConverterSelector formulaConverter = ConverterSelector.parse("collection(text, number, date-time, basic, spreadsheet-value, boolean, error-throwing, color, expression, environment, locale, plugins, spreadsheet-metadata, style, text-node, template, net, form-and-validation)");

        return SpreadsheetMetadata.EMPTY
            .set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, SpreadsheetId.parse("1234"))
            .set(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME, SpreadsheetName.with("Untitled5678"))
            .set(SpreadsheetMetadataPropertyName.LOCALE, LOCALE)
            .loadFromLocale(
                LocaleContexts.jre(LOCALE)
            )
            .set(
                SpreadsheetMetadataPropertyName.AUDIT_INFO,
                AuditInfo.create(
                    EmailAddress.parse("creator@example.com"),
                    LocalDateTime.of(1999, 12, 31, 12, 58, 59)
                )
            ).set(SpreadsheetMetadataPropertyName.CELL_CHARACTER_WIDTH, 1)
            .set(SpreadsheetMetadataPropertyName.DATE_TIME_OFFSET, Converters.EXCEL_1904_DATE_SYSTEM_OFFSET)
            .set(
                SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_DIGIT_COUNT,
                DecimalNumberContext.DEFAULT_NUMBER_DIGIT_COUNT
            ).set(SpreadsheetMetadataPropertyName.DEFAULT_YEAR, 20)
            .set(
                SpreadsheetMetadataPropertyName.ERROR_FORMATTER,
                SpreadsheetFormatterSelector.parse("badge-error default-text")
            ).set(SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND, EXPRESSION_NUMBER_KIND)
            .set(
                SpreadsheetMetadataPropertyName.FORMULA_CONVERTER,
                formulaConverter
            ).set(
                SpreadsheetMetadataPropertyName.FORMULA_FUNCTIONS,
                EXPRESSION_FUNCTION_PROVIDER.expressionFunctionInfos()
                    .aliasSet()
            ).set(
                SpreadsheetMetadataPropertyName.FORMATTING_CONVERTER,
                ConverterSelector.parse("collection(text, number, date-time, basic, spreadsheet-value, boolean, error-throwing, color, expression, environment, locale, plugins, spreadsheet-metadata, style, text-node, template, net)")
            ).set(SpreadsheetMetadataPropertyName.PRECISION, MathContext.DECIMAL32.getPrecision())
            .set(SpreadsheetMetadataPropertyName.ROUNDING_MODE, RoundingMode.HALF_UP)
            .set(SpreadsheetMetadataPropertyName.NUMBER_FORMATTER, SpreadsheetPattern.parseNumberFormatPattern("#.###").spreadsheetFormatterSelector())
            .set(
                SpreadsheetMetadataPropertyName.SCRIPTING_CONVERTER,
                formulaConverter
            ).set(
                SpreadsheetMetadataPropertyName.SCRIPTING_FUNCTIONS,
                EXPRESSION_FUNCTION_PROVIDER.expressionFunctionInfos()
                    .aliasSet() // spreadsheetMetadataSet
            ).set(SpreadsheetMetadataPropertyName.TEXT_FORMATTER, SpreadsheetPattern.parseTextFormatPattern("@@").spreadsheetFormatterSelector())
            .set(SpreadsheetMetadataPropertyName.TWO_DIGIT_YEAR, 20)
            .set(
                SpreadsheetMetadataPropertyName.STYLE,
                TextStyle.EMPTY.set(TextStylePropertyName.WIDTH, Length.pixel(50.0))
                    .set(TextStylePropertyName.HEIGHT, Length.pixel(50.0))
            ).set(
                SpreadsheetMetadataPropertyName.COMPARATORS,
                SpreadsheetComparatorAliasSet.EMPTY
            ).set(
                SpreadsheetMetadataPropertyName.CONVERTERS,
                SpreadsheetConvertersConverterProviders.ALL.aliasSet()
            ).set(
                SpreadsheetMetadataPropertyName.EXPORTERS,
                SpreadsheetExporterAliasSet.EMPTY
            ).set(
                SpreadsheetMetadataPropertyName.FORM_HANDLERS,
                FormHandlerAliasSet.EMPTY
            ).set(
                SpreadsheetMetadataPropertyName.FORMATTERS,
                SpreadsheetFormatterAliasSet.EMPTY
            ).set(
                SpreadsheetMetadataPropertyName.FUNCTIONS,
                SpreadsheetExpressionFunctionProviders.ALL.aliasSet()
            ).set(
                SpreadsheetMetadataPropertyName.IMPORTERS,
                SpreadsheetImporterAliasSet.EMPTY
            ).set(
                SpreadsheetMetadataPropertyName.PARSERS,
                SpreadsheetParserAliasSet.EMPTY
            ).set(
                SpreadsheetMetadataPropertyName.VALIDATORS,
                ValidatorAliasSet.EMPTY
            ).set(
                SpreadsheetMetadataPropertyName.VALIDATION_CONVERTER,
                ConverterSelector.parse("basic")
            );
    }

    private SpreadsheetMetadata metadataWithStrangeNumberFormatPattern() {
        return this.metadata()
            .set(SpreadsheetMetadataPropertyName.NUMBER_FORMATTER, SpreadsheetPattern.parseNumberFormatPattern("\"Number:\"#.###").spreadsheetFormatterSelector());
    }

    private SpreadsheetEngineContext evaluateAndCheck(final SpreadsheetCell save,
                                                      final Map<String, String> preload,
                                                      final SpreadsheetMetadata metadata,
                                                      final Optional<?> expectedValue,
                                                      final Optional<TextNode> formatted) {
        return this.evaluateAndCheck(
            save,
            preload,
            metadata,
            expectedValue,
            formatted,
            TerminalContexts.fake()
        );
    }

    private SpreadsheetEngineContext evaluateAndCheck(final SpreadsheetCell save,
                                                      final Map<String, String> preload,
                                                      final SpreadsheetMetadata metadata,
                                                      final Optional<?> expectedValue,
                                                      final Optional<TextNode> formatted,
                                                      final TerminalContext terminalContext) {
        final SpreadsheetEngine engine = SpreadsheetEngines.basic();

        final SpreadsheetEngineContext context = this.spreadsheetEngineContext(
            save.formula()
                .text()
                .contains("spreadsheetMetadataSet") ? // HACK: testEvaluateSpreadsheetMetadataSet if FORMULA spreadsheetMetadataSet will fail because environment is readonly
                SpreadsheetMetadataMode.SCRIPTING :
                SpreadsheetMetadataMode.FORMULA,
            metadata,
            EnvironmentContexts.map(SPREADSHEET_ENVIRONMENT_CONTEXT),
            terminalContext,
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

        final SpreadsheetCellReference cellReference = save.reference();
        final SpreadsheetFormula cellFormula = save.formula();

        final SpreadsheetCell saved = engine.saveCell(
                save,
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
                    .errorOrValue(),
                cellReference + "=" + cellFormula + "\n" +
                    preload.entrySet().stream()
                        .map(e -> e.getKey() + "=" + e.getValue())
                        .collect(Collectors.joining("\n"))
            );
        }

        return context;
    }

    private SpreadsheetEngineContext spreadsheetEngineContext(final SpreadsheetMetadataMode mode,
                                                              final SpreadsheetMetadata spreadsheetMetadata,
                                                              final EnvironmentContext environmentContext,
                                                              final TerminalContext terminalContext,
                                                              final ProviderContext providerContext) {
        final SpreadsheetMetadataStore metadataStore = SpreadsheetMetadataStores.treeMap();
        final SpreadsheetMetadata saved = metadataStore.save(spreadsheetMetadata);

        return SpreadsheetEngineContexts.basic(
            mode,
            SpreadsheetContexts.basic(
                (id) -> SpreadsheetStoreRepositories.treeMap(metadataStore),
                SpreadsheetProviders.basic(
                    SpreadsheetConvertersConverterProviders.spreadsheetConverters(
                        (ProviderContext p) -> spreadsheetMetadata.dateTimeConverter(
                            SPREADSHEET_FORMATTER_PROVIDER,
                            SPREADSHEET_PARSER_PROVIDER,
                            p
                        )
                    ),
                    EXPRESSION_FUNCTION_PROVIDER,
                    SPREADSHEET_COMPARATOR_PROVIDER,
                    SPREADSHEET_EXPORTER_PROVIDER,
                    SPREADSHEET_FORMATTER_PROVIDER,
                    FORM_HANDLER_PROVIDER,
                    SPREADSHEET_IMPORTER_PROVIDER,
                    SPREADSHEET_PARSER_PROVIDER,
                    VALIDATOR_PROVIDER
                ),
                SPREADSHEET_ENGINE_CONTEXT_FACTORY,
                HATEOS_ROUTER_FACTORY,
                SpreadsheetEnvironmentContexts.basic(
                    environmentContext.setEnvironmentValue(
                        SpreadsheetEnvironmentContext.SERVER_URL,
                        SERVER_URL
                    ).setEnvironmentValue(
                        SpreadsheetEnvironmentContext.SPREADSHEET_ID,
                        saved.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID)
                    )
                ),
                LOCALE_CONTEXT,
                providerContext,
                TERMINAL_SERVER_CONTEXT
            ),
            terminalContext
        );
    }

    // isPure..........................................................................................................

    // HAS_NOW()
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

        final List<ExpressionFunction<?, SpreadsheetExpressionEvaluationContext>> pureFunctions = Lists.array();
        final ExpressionFunctionProvider<SpreadsheetExpressionEvaluationContext> provider = SpreadsheetExpressionFunctionProviders.expressionFunctionProvider(walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY);

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
                        case "getenv":
                        case "getlocale":
                        case "getuser":
                        case "print":
                        case "printenv":
                        case "println":
                        case "readline":
                        case "removeenv":
                        case "setenv":
                        case "setlocale":
                        case "getvalidator":
                        case "validationerrorif":
                        case "validationvalue":
                        case "exit":
                        case "shell":
                            pure = false;
                            break;
                        default:
                            pure = true;
                            break;
                    }

                    final ExpressionFunction<?, SpreadsheetExpressionEvaluationContext> function = provider.expressionFunction(
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
        SpreadsheetExpressionFunctionProviders.expressionFunctionProvider(walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY)
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

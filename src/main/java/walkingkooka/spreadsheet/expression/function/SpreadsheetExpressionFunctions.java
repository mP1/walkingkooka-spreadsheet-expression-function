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

import walkingkooka.Cast;
import walkingkooka.color.Color;
import walkingkooka.color.expression.function.ColorExpressionFunctions;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctions;
import walkingkooka.tree.expression.function.booleann.BooleanExpressionFunctions;
import walkingkooka.tree.expression.function.datetime.DateTimeExpressionFunctions;
import walkingkooka.tree.expression.function.engineering.EngineeringExpressionFunctions;
import walkingkooka.tree.expression.function.number.NumberExpressionFunctions;
import walkingkooka.tree.expression.function.number.trigonometry.NumberTrigonomteryExpressionFunctions;
import walkingkooka.tree.expression.function.stat.StatExpressionFunctions;
import walkingkooka.tree.expression.function.string.StringExpressionFunctions;
import walkingkooka.tree.text.Hyperlink;
import walkingkooka.tree.text.Image;
import walkingkooka.tree.text.Styleable;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.expression.function.TreeTextExpressionFunctions;
import walkingkooka.validation.ValidationError;
import walkingkooka.validation.provider.ValidatorSelector;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Collection of static factory methods for numerous {@link ExpressionFunction}.
 */
public final class SpreadsheetExpressionFunctions implements PublicStaticHelper {

    /**
     * Constant that holds the OR function which is used to combine multiple query tests.
     */
    public final static ExpressionFunctionName OR = cellFindWizardHelperFunction(
            or()
    );

    /**
     * Constant that holds the textMatch function to be used by the find wizard when building the BIG query from cell component contains text boxes.
     */
    public final static ExpressionFunctionName TEXT_MATCH = cellFindWizardHelperFunction(
            textMatch()
    );

    /**
     * The function name of the getter that retrieve the {@link SpreadsheetCell#dateTimeSymbols()} within a find.
     */
    public final static ExpressionFunctionName CELL_DATE_TIME_SYMBOLS = cellFindWizardHelperFunction(
            cellDateTimeSymbols()
    );

    /**
     * The function name of the getter that retrieve the {@link SpreadsheetCell#decimalNumberSymbols()}  within a find.
     */
    public final static ExpressionFunctionName CELL_DECIMAL_NUMBER_SYMBOLS = cellFindWizardHelperFunction(
            cellDecimalNumberSymbols()
    );

    /**
     * The function name of the getter that retrieve the {@link SpreadsheetCell#formula()} within a find.
     */
    public final static ExpressionFunctionName CELL_FORMULA = cellFindWizardHelperFunction(
            cellFormula()
    );

    /**
     * The function name of the getter that retrieve the {@link SpreadsheetCell#formatter()} within a find.
     */
    public final static ExpressionFunctionName CELL_FORMATTER = cellFindWizardHelperFunction(
            cellFormatter()
    );

    /**
     * The function name of the getter that retrieve the {@link SpreadsheetCell#parser()} within a find.
     */
    public final static ExpressionFunctionName CELL_PARSER = cellFindWizardHelperFunction(
            cellParser()
    );

    /**
     * The function name of the getter that retrieve the {@link SpreadsheetCell#style()} within a find.
     */
    public final static ExpressionFunctionName CELL_STYLE = cellFindWizardHelperFunction(
            cellStyle()
    );

    /**
     * The function name of the getter that retrieve the {@link SpreadsheetFormula#value()} within a find.
     */
    public final static ExpressionFunctionName CELL_VALUE = cellFindWizardHelperFunction(
            cellValue()
    );

    /**
     * The function name of the getter that retrieve the {@link SpreadsheetFormula#valueType()} within a find.
     */
    public final static ExpressionFunctionName CELL_VALUE_TYPE = cellFindWizardHelperFunction(
            cellValueType()
    );

    /**
     * The function name of the getter that retrieve the {@link SpreadsheetCell#formattedValue()} within a find.
     */
    public final static ExpressionFunctionName CELL_FORMATTED_VALUE = cellFindWizardHelperFunction(
            cellFormattedValue()
    );

    /**
     * The function name of the getter that retrieve the {@link SpreadsheetCell#formattedValue()} within a find.
     */
    public final static ExpressionFunctionName CELL_VALIDATOR = cellFindWizardHelperFunction(
            cellValidator()
    );

    private static ExpressionFunctionName cellFindWizardHelperFunction(final ExpressionFunction<?, ?> function) {
        final ExpressionFunctionName name = function.name()
                .orElseThrow(() -> new IllegalStateException("Missing function for " + function));
        return name.setCaseSensitivity(
                walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY
        );
    }

    /**
     * {@see NumberExpressionFunctions#abs}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> abs() {
        return fixName(
                NumberExpressionFunctions.abs()
        );
    }

    /**
     * {@see NumberTrigonomteryExpressionFunctions#acos}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> acos() {
        return fixName(
                NumberTrigonomteryExpressionFunctions.acos()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctions#address}
     */
    public static ExpressionFunction<SpreadsheetCellReference, SpreadsheetExpressionEvaluationContext> address() {
        return SpreadsheetExpressionFunctionAddress.INSTANCE;
    }

    /**
     * {@see BooleanExpressionFunctions#and}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> and() {
        return fixName(
                BooleanExpressionFunctions.and()
        );
    }

    /**
     * {@see NumberTrigonomteryExpressionFunctions#asin}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> asin() {
        return fixName(
                NumberTrigonomteryExpressionFunctions.asin()
        );
    }

    /**
     * {@see NumberTrigonomteryExpressionFunctions#atan}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> atan() {
        return fixName(
                NumberTrigonomteryExpressionFunctions.atan()
        );
    }

    /**
     * {@see StatExpressionFunctions#average}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> average() {
        return fixName(
                StatExpressionFunctions.average()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctions#averageIf}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> averageIf() {
        return SpreadsheetExpressionFunctionNumberIf.averageIf();
    }

    /**
     * {@see NumberExpressionFunctions#base}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> base() {
        return fixName(
                NumberExpressionFunctions.base()
        );
    }

    /**
     * {@see EngineeringExpressionFunctions#bin2dec}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> bin2dec() {
        return fixName(
                EngineeringExpressionFunctions.bin2dec()
        );
    }

    /**
     * {@see EngineeringExpressionFunctions#bin2hex}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> bin2hex() {
        return fixName(
                EngineeringExpressionFunctions.bin2hex()
        );
    }

    /**
     * {@see EngineeringExpressionFunctions#bin2oct}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> bin2oct() {
        return fixName(
                EngineeringExpressionFunctions.bin2oct()
        );
    }

    /**
     * {@see EngineeringExpressionFunctions#bitAnd}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> bitAnd() {
        return BITAND;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> BITAND = fixName(
            EngineeringExpressionFunctions.bitAnd()
    );

    /**
     * {@see EngineeringExpressionFunctions#bitOr}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> bitOr() {
        return BITOR;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> BITOR = fixName(
            EngineeringExpressionFunctions.bitOr()
    );

    /**
     * {@see EngineeringExpressionFunctions#bitXor}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> bitXor() {
        return BitXor;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> BitXor = fixName(
            EngineeringExpressionFunctions.bitXor()
    );

    /**
     * {@see NumberExpressionFunctions#ceil}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> ceil() {
        return fixName(
                NumberExpressionFunctions.ceil()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctions#cell}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> cell() {
        return SpreadsheetExpressionFunctionObjectCell.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionCellDateTimeSymbols}
     */
    public static ExpressionFunction<DateTimeSymbols, SpreadsheetExpressionEvaluationContext> cellDateTimeSymbols() {
        return SpreadsheetExpressionFunctionCellDateTimeSymbols.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionCellDecimalNumberSymbols}
     */
    public static ExpressionFunction<DecimalNumberSymbols, SpreadsheetExpressionEvaluationContext> cellDecimalNumberSymbols() {
        return SpreadsheetExpressionFunctionCellDecimalNumberSymbols.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionCellFormattedValue}
     */
    public static ExpressionFunction<TextNode, SpreadsheetExpressionEvaluationContext> cellFormattedValue() {
        return SpreadsheetExpressionFunctionCellFormattedValue.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionCellFormatter}
     */
    public static ExpressionFunction<SpreadsheetFormatterSelector, SpreadsheetExpressionEvaluationContext> cellFormatter() {
        return SpreadsheetExpressionFunctionCellFormatter.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionCellFormula}
     */
    public static ExpressionFunction<SpreadsheetFormula, SpreadsheetExpressionEvaluationContext> cellFormula() {
        return SpreadsheetExpressionFunctionCellFormula.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionCellParser}
     */
    public static ExpressionFunction<SpreadsheetParserSelector, SpreadsheetExpressionEvaluationContext> cellParser() {
        return SpreadsheetExpressionFunctionCellParser.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionCellStyle}
     */
    public static ExpressionFunction<TextStyle, SpreadsheetExpressionEvaluationContext> cellStyle() {
        return SpreadsheetExpressionFunctionCellStyle.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionObjectCellValidator}
     */
    public static ExpressionFunction<ValidatorSelector, SpreadsheetExpressionEvaluationContext> cellValidator() {
        return SpreadsheetExpressionFunctionCellValidator.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionObjectCellValue}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> cellValue() {
        return SpreadsheetExpressionFunctionCellValue.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionObjectCellValueType}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> cellValueType() {
        return SpreadsheetExpressionFunctionCellValueType.INSTANCE;
    }

    /**
     * {@see StringExpressionFunctions#ascii}
     */
    public static ExpressionFunction<Character, SpreadsheetExpressionEvaluationContext> charFunction() {
        return CHAR_FUNCTION;
    }

    private final static ExpressionFunction<Character, SpreadsheetExpressionEvaluationContext> CHAR_FUNCTION = StringExpressionFunctions.<SpreadsheetExpressionEvaluationContext>ascii()
            .setName(functionName("char"));

    /**
     * {@see BooleanExpressionFunctions#choose}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> choose() {
        return fixName(
                BooleanExpressionFunctions.choose()
        );
    }

    /**
     * {@see StringExpressionFunctions#clean}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> clean() {
        return fixName(
                StringExpressionFunctions.clean()
        );
    }

    /**
     * {@see StringExpressionFunctions#unicode}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> code() {
        return CODE;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> CODE = StringExpressionFunctions.<SpreadsheetExpressionEvaluationContext>unicode()
            .setName(functionName("code"));

    /**
     * {@see ColorExpressionFunctions.#color}
     */
    public static ExpressionFunction<Color, SpreadsheetExpressionEvaluationContext> color() {
        return fixName(
                ColorExpressionFunctions.color()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberColumnOrRow#COLUMN}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> column() {
        return SpreadsheetExpressionFunctionNumberColumnOrRow.COLUMN;
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberColumnsOrRows#COLUMNS}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> columns() {
        return SpreadsheetExpressionFunctionNumberColumnsOrRows.COLUMNS;
    }

    /**
     * {@see StringExpressionFunctions#concat}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> concat() {
        return CONCAT;
    }

    private final static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> CONCAT = SpreadsheetExpressionFunctionUnformattedNumber.with(
            StringExpressionFunctions.<SpreadsheetExpressionEvaluationContext>concat()
                    .filterParameterValues(SpreadsheetExpressionFunctions::filterNonNullAndNotMissingCell)
    );

    /**
     * {@see NumberTrigonomteryExpressionFunctions#cos}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> cos() {
        return fixName(
                NumberTrigonomteryExpressionFunctions.cos()
        );
    }

    /**
     * Counts the {@link ExpressionNumber} present in the parameter values
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> count() {
        return COUNT;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> COUNT = fixName(
            StatExpressionFunctions.<SpreadsheetExpressionEvaluationContext>count()
                    .filterParameterValues(SpreadsheetExpressionFunctions::filterNumbers)
    );

    private static boolean filterNumbers(final Object value,
                                         final SpreadsheetExpressionEvaluationContext context) {
        return value instanceof ExpressionNumber |
                value instanceof LocalDate |
                value instanceof LocalDateTime |
                value instanceof LocalTime;
    }

    /**
     * Counts the values present in the parameter values, skipping missing or null values.
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> countA() {
        return COUNTA;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> COUNTA = StatExpressionFunctions.<SpreadsheetExpressionEvaluationContext>count()
            .filterParameterValues(SpreadsheetExpressionFunctions::filterNonNullAndNotMissingCell)
            .setName(functionName("countA"));

    /**
     * Counts the missing values or cells or null values
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> countBlank() {
        return SpreadsheetExpressionFunctionNumberCountBlank.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberIf#countIf}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> countIf() {
        return SpreadsheetExpressionFunctionNumberIf.countIf();
    }

    /**
     * {@see DateTimeExpressionFunctions#date}
     */
    public static ExpressionFunction<LocalDate, SpreadsheetExpressionEvaluationContext> date() {
        return fixName(
                DateTimeExpressionFunctions.date()
        );
    }

    /**
     * {@see DateTimeExpressionFunctions#day}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> day() {
        return fixName(
                DateTimeExpressionFunctions.day()
        );
    }

    /**
     * {@see DateTimeExpressionFunctions#days}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> days() {
        return fixName(
                DateTimeExpressionFunctions.days()
        );
    }

    /**
     * {@see EngineeringExpressionFunctions#dec2bin}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> dec2bin() {
        return fixName(
                EngineeringExpressionFunctions.dec2bin()
        );
    }

    /**
     * {@see EngineeringExpressionFunctions#dec2hex}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> dec2hex() {
        return fixName(
                EngineeringExpressionFunctions.dec2hex()
        );
    }

    /**
     * {@see EngineeringExpressionFunctions#dec2oct}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> dec2oct() {
        return fixName(
                EngineeringExpressionFunctions.dec2oct()
        );
    }

    /**
     * {@see NumberExpressionFunctions#decimal}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> decimal() {
        return fixName(
                NumberExpressionFunctions.decimal()
        );
    }

    /**
     * {@see NumberTrigonomteryExpressionFunctions#degrees}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> degrees() {
        return fixName(
                NumberTrigonomteryExpressionFunctions.degrees()
        );
    }

    /**
     * {@see EngineeringExpressionFunctions#delta}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> delta() {
        return fixName(
                EngineeringExpressionFunctions.delta()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionStringDollar}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> dollar() {
        return SpreadsheetExpressionFunctionStringDollar.INSTANCE;
    }

    /**
     * {@see NumberExpressionFunctions#e}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> e() {
        return fixName(
                NumberExpressionFunctions.e()
        );
    }

    /**
     * {@see walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions#error}
     */
    public static ExpressionFunction<SpreadsheetError, SpreadsheetExpressionEvaluationContext> error() {
        return Cast.to(
                walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.error()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionObjectEval}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> eval() {
        return SpreadsheetExpressionFunctionObjectEval.INSTANCE;
    }

    /**
     * {@see NumberExpressionFunctions#even}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> even() {
        return fixName(
                NumberExpressionFunctions.even()
        );
    }

    /**
     * {@see StringExpressionFunctions#equalsCaseSensitive}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> exact() {
        return EXACT;
    }

    private final static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> EXACT = StringExpressionFunctions.<SpreadsheetExpressionEvaluationContext>equalsCaseSensitive()
            .setName(functionName("exact"));

    /**
     * {@see NumberExpressionFunctions#exp}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> exp() {
        return fixName(
                NumberExpressionFunctions.exp()
        );
    }

    /**
     * {@see BooleanExpressionFunctions#falseFunction}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> falseFunction() {
        return fixName(
                BooleanExpressionFunctions.falseFunction()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionObjectFind}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> find() {
        return SpreadsheetExpressionFunctionObjectFind.INSTANCE;
    }

    /**
     * {@see NumberExpressionFunctions#fixed()}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> fixed() {
        return fixName(
                NumberExpressionFunctions.fixed()
        );
    }

    /**
     * {@see NumberExpressionFunctions#floor}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> floor() {
        return fixName(
                NumberExpressionFunctions.floor()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionStringFormulaText}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> formulaText() {
        return SpreadsheetExpressionFunctionStringFormulaText.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionTextNodeFormatValue}
     */
    public static ExpressionFunction<TextNode, SpreadsheetExpressionEvaluationContext> formatValue() {
        return SpreadsheetExpressionFunctionTextNodeFormatValue.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionObjectGetFormatValue}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> getFormatValue() {
        return SpreadsheetExpressionFunctionObjectGetFormatValue.INSTANCE;
    }

    /**
     * {@see TreeTextExpressionFunctions#getStyle}
     */
    public static ExpressionFunction<TextStyle, SpreadsheetExpressionEvaluationContext> getStyle() {
        return fixName(
                TreeTextExpressionFunctions.getStyle()
        );
    }

    /**
     * {@see EngineeringExpressionFunctions#hex2bin}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> hex2bin() {
        return fixName(
                EngineeringExpressionFunctions.hex2bin()
        );
    }

    /**
     * {@see EngineeringExpressionFunctions#hex2dec}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> hex2dec() {
        return fixName(
                EngineeringExpressionFunctions.hex2dec()
        );
    }

    /**
     * {@see EngineeringExpressionFunctions#hex2oct}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> hex2oct() {
        return fixName(
                EngineeringExpressionFunctions.hex2oct()
        );
    }

    /**
     * {@see DateTimeExpressionFunctions#hour}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> hour() {
        return fixName(
                DateTimeExpressionFunctions.hour()
        );
    }

    /**
     * {@see TreeTextExpressionFunctions#hyperlink}
     */
    public static ExpressionFunction<Hyperlink, SpreadsheetExpressionEvaluationContext> hyperlink() {
        return fixName(
                TreeTextExpressionFunctions.hyperlink()
        );
    }

    /**
     * {@see BooleanExpressionFunctions#ifFunction}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> ifFunction() {
        return fixName(
                BooleanExpressionFunctions.ifFunction()
        );
    }

    /**
     * {@see BooleanExpressionFunctions#ifs}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> ifs() {
        return fixName(
                BooleanExpressionFunctions.ifs()
        );
    }

    /**
     * {@see TreeTextExpressionFunctions#image}
     */
    public static ExpressionFunction<Image, SpreadsheetExpressionEvaluationContext> image() {
        return fixName(
                TreeTextExpressionFunctions.image()
        );
    }
    
    /**
     * {@see SpreadsheetExpressionFunctionIndirect}
     */
    public static ExpressionFunction<SpreadsheetCellReference, SpreadsheetExpressionEvaluationContext> indirect() {
        return SpreadsheetExpressionFunctionIndirect.INSTANCE;
    }

    /**
     * {@see NumberExpressionFunctions#intFunction}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> intFunction() {
        return fixName(
                NumberExpressionFunctions.intFunction()
        );
    }

    /**
     * {@see ColorExpressionFunctions.#invertColor}
     */
    public static ExpressionFunction<Color, SpreadsheetExpressionEvaluationContext> invertColor() {
        return fixName(
                ColorExpressionFunctions.invertColor()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionBooleanIsBlank}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isBlank() {
        return SpreadsheetExpressionFunctionBooleanIsBlank.INSTANCE;
    }

    /**
     * {@see DateTimeExpressionFunctions#isDate}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isDate() {
        return fixName(
                DateTimeExpressionFunctions.isDate()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionBooleanIsErrErrorNa#isErr}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isErr() {
        return SpreadsheetExpressionFunctionBooleanIsErrErrorNa.isErr();
    }

    /**
     * {@see SpreadsheetExpressionFunctionBooleanIsErrErrorNa#isError}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isError() {
        return SpreadsheetExpressionFunctionBooleanIsErrErrorNa.isError();
    }

    /**
     * {@see NumberExpressionFunctions#isEven}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isEven() {
        return fixName(
                NumberExpressionFunctions.isEven()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionBooleanIsFormula}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isFormula() {
        return SpreadsheetExpressionFunctionBooleanIsFormula.INSTANCE;
    }

    /**
     * {@see BooleanExpressionFunctions#isBoolean}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isLogical() {
        return IS_LOGICAL;
    }

    private final static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> IS_LOGICAL = BooleanExpressionFunctions.<SpreadsheetExpressionEvaluationContext>isBoolean()
            .setName(functionName("isLogical"));

    /**
     * {@see SpreadsheetExpressionFunctionBooleanIsErrErrorNa#isNs}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isNa() {
        return SpreadsheetExpressionFunctionBooleanIsErrErrorNa.isNa();
    }

    /**
     * {@see StringExpressionFunctions#isNonText}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isNonText() {
        return fixName(
                StringExpressionFunctions.isNonText()
        );
    }

    /**
     * {@see BooleanExpressionFunctions#isNull}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isNull() {
        return fixName(
                BooleanExpressionFunctions.isNull()
        );
    }

    /**
     * {@see NumberExpressionFunctions#isNumber}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isNumber() {
        return fixName(
                NumberExpressionFunctions.isNumber()
        );
    }

    /**
     * {@see NumberExpressionFunctions#isOdd}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isOdd() {
        return fixName(
                NumberExpressionFunctions.isOdd()
        );
    }

    /**
     * {@see DateTimeExpressionFunctions.isoWeekNum}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> isoWeekNum() {
        return fixName(
                DateTimeExpressionFunctions.isoWeekNum()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionBooleanIsRef}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isRef() {
        return SpreadsheetExpressionFunctionBooleanIsRef.INSTANCE;
    }

    /**
     * {@see StringExpressionFunctions#isText}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isText() {
        return fixName(
                StringExpressionFunctions.isText()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionLambda}
     */
    public static ExpressionFunction<ExpressionFunction<?, SpreadsheetExpressionEvaluationContext>, SpreadsheetExpressionEvaluationContext> lambda() {
        return SpreadsheetExpressionFunctionLambda.INSTANCE;
    }

    /**
     * {@see StringExpressionFunctions#left}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> left() {
        return fixName(
                StringExpressionFunctions.left()
        );
    }

    /**
     * {@see StringExpressionFunctions#stringLength}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> len() {
        return LEN;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> LEN = unformattedNumber(
            StringExpressionFunctions.stringLength(),
            "len"
    );

    /**
     * {@see SpreadsheetExpressionFunctionObjectLet}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> let() {
        return SpreadsheetExpressionFunctionObjectLet.INSTANCE;
    }

    /**
     * {@see NumberExpressionFunctions#ln}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> ln() {
        return fixName(
                NumberExpressionFunctions.ln()
        );
    }

    /**
     * {@see NumberExpressionFunctions#log}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> log() {
        return fixName(
                NumberExpressionFunctions.log()
        );
    }

    /**
     * {@see NumberExpressionFunctions#log10}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> log10() {
        return fixName(
                NumberExpressionFunctions.log10()
        );
    }

    /**
     * {@see StringExpressionFunctions#lower}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> lower() {
        return LOWER;
    }

    private final static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> LOWER = unformattedNumber(
            StringExpressionFunctions.lowerCase(),
            "lower"
    );

    /**
     * {@see StatExpressionFunctions#max}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> max() {
        return fixName(
                StatExpressionFunctions.max()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberIf.maxIf}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> maxIf() {
        return SpreadsheetExpressionFunctionNumberIf.maxIf();
    }

    /**
     * {@see TreeTextExpressionFunctions#mergeStyle}
     */
    public static ExpressionFunction<Styleable, SpreadsheetExpressionEvaluationContext> mergeStyle() {
        return fixName(
                TreeTextExpressionFunctions.mergeStyle()
        );
    }

    /**
     * {@see StringExpressionFunctions#mid}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> mid() {
        return fixName(
                StringExpressionFunctions.mid()
        );
    }

    /**
     * {@see StatExpressionFunctions#min}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> min() {
        return fixName(
                StatExpressionFunctions.min()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberIf.minIf}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> minIf() {
        return SpreadsheetExpressionFunctionNumberIf.minIf();
    }

    /**
     * {@see DateTimeExpressionFunctions#minute}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> minute() {
        return fixName(
                DateTimeExpressionFunctions.minute()
        );
    }

    /**
     * {@see ColorExpressionFunctions.#mixColor}
     */
    public static ExpressionFunction<Color, SpreadsheetExpressionEvaluationContext> mixColor() {
        return fixName(
                ColorExpressionFunctions.mixColor()
        );
    }
    
    /**
     * {@see NumberExpressionFunctions#mod}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> mod() {
        return fixName(
                NumberExpressionFunctions.mod()
        );
    }

    /**
     * {@see DateTimeExpressionFunctions#month}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> month() {
        return fixName(
                DateTimeExpressionFunctions.month()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionNextEmptyColumn#not}
     */
    public static ExpressionFunction<SpreadsheetColumnReference, SpreadsheetExpressionEvaluationContext> nextEmptyColumn() {
        return SpreadsheetExpressionFunctionNextEmptyColumn.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionNextEmptyRow}
     */
    public static ExpressionFunction<SpreadsheetRowReference, SpreadsheetExpressionEvaluationContext> nextEmptyRow() {
        return SpreadsheetExpressionFunctionNextEmptyRow.INSTANCE;
    }

    /**
     * {@see BooleanExpressionFunctions#not}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> not() {
        return fixName(
                BooleanExpressionFunctions.not()
        );
    }

    /**
     * {@see DateTimeExpressionFunctions#now}
     */
    public static ExpressionFunction<LocalDateTime, SpreadsheetExpressionEvaluationContext> now() {
        return fixName(
                DateTimeExpressionFunctions.now()
        );
    }

    /**
     * {@see ExpressionFunctions#nullFunction}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> nullFunction() {
        return fixName(
                ExpressionFunctions.nullFunction()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberValue}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> numberValue() {
        return SpreadsheetExpressionFunctionNumberValue.INSTANCE;
    }

    /**
     * {@see EngineeringExpressionFunctions#oct2bin}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> oct2bin() {
        return fixName(
                EngineeringExpressionFunctions.oct2bin()
        );
    }

    /**
     * {@see EngineeringExpressionFunctions#oct2dec}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> oct2dec() {
        return fixName(
                EngineeringExpressionFunctions.oct2dec()
        );
    }

    /**
     * {@see EngineeringExpressionFunctions#oct2hex}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> oct2hex() {
        return fixName(
                EngineeringExpressionFunctions.oct2hex()
        );
    }

    /**
     * {@see NumberExpressionFunctions#odd}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> odd() {
        return fixName(
                NumberExpressionFunctions.odd()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionOffset}
     */
    public static ExpressionFunction<SpreadsheetExpressionReference, SpreadsheetExpressionEvaluationContext> offset() {
        return SpreadsheetExpressionFunctionOffset.INSTANCE;
    }

    /**
     * {@see BooleanExpressionFunctions#or}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> or() {
        return fixName(
                BooleanExpressionFunctions.or()
        );
    }

    /**
     * {@see NumberExpressionFunctions#pi}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> pi() {
        return fixName(
                NumberExpressionFunctions.pi()
        );
    }

    /**
     * {@see NumberExpressionFunctions#product}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> product() {
        return fixName(
                NumberExpressionFunctions.product()
        );
    }

    /**
     * {@see StringExpressionFunctions#proper}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> proper() {
        return fixName(
                StringExpressionFunctions.proper()
        );
    }

    /**
     * {@see NumberExpressionFunctions#quotient}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> quotient() {
        return fixName(
                NumberExpressionFunctions.quotient()
        );
    }

    /**
     * {@see NumberTrigonomteryExpressionFunctions#radians}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> radians() {
        return fixName(
                NumberTrigonomteryExpressionFunctions.radians()
        );
    }

    /**
     * {@see NumberExpressionFunctions#random}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> rand() {
        return RAND;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> RAND = NumberExpressionFunctions.<SpreadsheetExpressionEvaluationContext>random()
            .setName(functionName("rand"));

    /**
     * {@see NumberExpressionFunctions#randomBetween}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> randBetween() {
        return RANDBETWEEN;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> RANDBETWEEN = NumberExpressionFunctions.<SpreadsheetExpressionEvaluationContext>randomBetween()
            .setName(functionName("randBetween"));

    /**
     * {@see StringExpressionFunctions#replace}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> replace() {
        return fixName(
                StringExpressionFunctions.replace()
        );
    }

    /**
     * {@see StringExpressionFunctions#rept}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> rept() {
        return REPT;
    }

    private final static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> REPT = StringExpressionFunctions.<SpreadsheetExpressionEvaluationContext>repeat()
            .setName(functionName("rept"));

    /**
     * {@see StringExpressionFunctions#right}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> right() {
        return fixName(
                StringExpressionFunctions.right()
        );
    }

    /**
     * {@see NumberExpressionFunctions#roman}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> roman() {
        return fixName(
                NumberExpressionFunctions.roman()
        );
    }

    /**
     * {@see NumberExpressionFunctions#roundHalf}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> round() {
        return ROUND;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> ROUND = NumberExpressionFunctions.<SpreadsheetExpressionEvaluationContext>roundHalf()
            .setName(functionName("round"));

    /**
     * {@see NumberExpressionFunctions#roundDown}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> roundDown() {
        return fixName(
                NumberExpressionFunctions.roundDown()
        );
    }

    /**
     * {@see NumberExpressionFunctions#roundUp}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> roundUp() {
        return fixName(
                NumberExpressionFunctions.roundUp()
        );
    }

    /**
     * {@see SSpreadsheetExpressionFunctionNumberColumnOrRow#ROW}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> row() {
        return SpreadsheetExpressionFunctionNumberColumnOrRow.ROW;
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberColumnsOrRows.ROWS}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> rows() {
        return SpreadsheetExpressionFunctionNumberColumnsOrRows.ROWS;
    }

    /**
     * {@see StringExpressionFunctions#searchCaseInsensitive}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> search() {
        return SEARCH;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> SEARCH = StringExpressionFunctions.<SpreadsheetExpressionEvaluationContext>searchCaseInsensitive()
            .setName(functionName("search"));

    /**
     * {@see DateTimeExpressionFunctions#second}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> second() {
        return fixName(
                DateTimeExpressionFunctions.second()
        );
    }

    /**
     * {@see TreeTextExpressionFunctions#setStyle}
     */
    public static ExpressionFunction<TextNode, SpreadsheetExpressionEvaluationContext> setStyle() {
        return fixName(
                TreeTextExpressionFunctions.setStyle()
        );
    }

    /**
     * {@see TreeTextExpressionFunctions#setText}
     */
    public static ExpressionFunction<TextNode, SpreadsheetExpressionEvaluationContext> setText() {
        return fixName(
                TreeTextExpressionFunctions.setText()
        );
    }

    /**
     * {@see NumberExpressionFunctions#sign}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> sign() {
        return fixName(
                NumberExpressionFunctions.sign()
        );
    }

    /**
     * {@see NumberTrigonomteryExpressionFunctions#sin}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> sin() {
        return fixName(
                NumberTrigonomteryExpressionFunctions.sin()
        );
    }

    /**
     * {@see NumberTrigonomteryExpressionFunctions#sinh}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> sinh() {
        return fixName(
                NumberTrigonomteryExpressionFunctions.sinh()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionSpreadsheetMetadataGet}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> spreadsheetMetadataGet() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataGet.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionSpreadsheetMetadataRemove}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> spreadsheetMetadataRemove() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataRemove.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionSpreadsheetMetadataSet}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> spreadsheetMetadataSet() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataSet.INSTANCE;
    }

    /**
     * {@see NumberExpressionFunctions#sqrt}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> sqrt() {
        return fixName(
                NumberExpressionFunctions.sqrt()
        );
    }

    /**
     * {@see TreeTextExpressionFunctions#style}
     */
    public static ExpressionFunction<TextStyle, SpreadsheetExpressionEvaluationContext> style() {
        return fixName(
                TreeTextExpressionFunctions.style()
        );
    }

    /**
     * {@see TreeTextExpressionFunctions#styleGet}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> styleGet() {
        return fixName(
                TreeTextExpressionFunctions.styleGet()
        );
    }

    /**
     * {@see TreeTextExpressionFunctions#styleRemove}
     */
    public static ExpressionFunction<Styleable, SpreadsheetExpressionEvaluationContext> styleRemove() {
        return fixName(
                TreeTextExpressionFunctions.styleRemove()
        );
    }

    /**
     * {@see TreeTextExpressionFunctions#styleSet}
     */
    public static ExpressionFunction<TextStyle, SpreadsheetExpressionEvaluationContext> styleSet() {
        return fixName(
                TreeTextExpressionFunctions.styleSet()
        );
    }

    /**
     * {@see TreeTextExpressionFunctions#styledText}
     */
    public static ExpressionFunction<TextNode, SpreadsheetExpressionEvaluationContext> styledText() {
        return fixName(
                TreeTextExpressionFunctions.styledText()
        );
    }

    /**
     * {@see StringExpressionFunctions#substitute}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> substitute() {
        return fixName(
                StringExpressionFunctions.substitute()
        );
    }

    /**
     * {@see StatExpressionFunctions#sum}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> sum() {
        return fixName(
                StatExpressionFunctions.sum()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberIf#sumIf}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> sumIf() {
        return SpreadsheetExpressionFunctionNumberIf.sumIf();
    }

    /**
     * {@see BooleanExpressionFunctions#switchFunction}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> switchFunction() {
        return fixName(
                BooleanExpressionFunctions.switchFunction()
        );
    }

    /**
     * {@see StringExpressionFunctions#t}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> t() {
        return fixName(
                StringExpressionFunctions.t()
        );
    }

    /**
     * {@see NumberTrigonomteryExpressionFunctions#tan}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> tan() {
        return fixName(
                NumberTrigonomteryExpressionFunctions.tan()
        );
    }

    /**
     * {@see NumberTrigonomteryExpressionFunctions#tanh}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> tanh() {
        return fixName(
                NumberTrigonomteryExpressionFunctions.tanh()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionStringTemplate#INSTANCE}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> template() {
        return SpreadsheetExpressionFunctionStringTemplate.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionStringText#INSTANCE}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> text() {
        return SpreadsheetExpressionFunctionStringText.INSTANCE;
    }

    /**
     * {@see StringExpressionFunctions#textJoin}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> textJoin() {
        return TEXTJOIN;
    }

    private final static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> TEXTJOIN = fixName(
            StringExpressionFunctions.textJoin()
    );

    /**
     * {@see SpreadsheetExpressionFunctionBooleanTextMatch}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> textMatch() {
        return SpreadsheetExpressionFunctionBooleanTextMatch.INSTANCE;
    }

    /**
     * {@see DateTimeExpressionFunctions#time}
     */
    public static ExpressionFunction<LocalTime, SpreadsheetExpressionEvaluationContext> time() {
        return fixName(
                DateTimeExpressionFunctions.time()
        );
    }

    /**
     * {@see DateTimeExpressionFunctions#today}
     */
    public static ExpressionFunction<LocalDate, SpreadsheetExpressionEvaluationContext> today() {
        return fixName(
                DateTimeExpressionFunctions.today()
        );
    }

    /**
     * {@see ColorExpressionFunctions#toRgbHexString}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> toRgbHexString() {
        return fixName(
                ColorExpressionFunctions.toRgbHexString()
        );
    }

    /**
     * {@see StringExpressionFunctions#trim}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> trim() {
        return TRIM;
    }

    private final static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> TRIM = StringExpressionFunctions.<SpreadsheetExpressionEvaluationContext>spaceTrim()
            .setName(functionName("trim"));

    /**
     * {@see BooleanExpressionFunctions#trueFunction}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> trueFunction() {
        return fixName(
                BooleanExpressionFunctions.trueFunction()
        );
    }

    /**
     * {@see NumberExpressionFunctions#trunc}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> trunc() {
        return fixName(
                NumberExpressionFunctions.trunc()
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberType#INSTANCE}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> type() {
        return SpreadsheetExpressionFunctionNumberType.INSTANCE;
    }

    /**
     * {@see StringExpressionFunctions#character}
     */
    public static ExpressionFunction<Character, SpreadsheetExpressionEvaluationContext> unichar() {
        return UNICHAR;
    }

    private final static ExpressionFunction<Character, SpreadsheetExpressionEvaluationContext> UNICHAR = StringExpressionFunctions.<SpreadsheetExpressionEvaluationContext>character()
            .setName(functionName("unichar"));

    /**
     * {@see StringExpressionFunctions#unicode}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> unicode() {
        return UNICODE;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> UNICODE = StringExpressionFunctions.<SpreadsheetExpressionEvaluationContext>unicode()
            .setName(functionName("unicode"));

    /**
     * {@see StringExpressionFunctions#upper}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> upper() {
        return UPPER;
    }

    private final static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> UPPER = unformattedNumber(
            StringExpressionFunctions.upperCase(),
            "upper"
    );

    /**
     * {@see SpreadsheetExpressionFunctionValidationError}
     */
    public static ExpressionFunction<ValidationError<SpreadsheetCellReference>, SpreadsheetExpressionEvaluationContext> validationError() {
        return SpreadsheetExpressionFunctionValidationError.INSTANCE;
    }

    /**
     * {@see NumberExpressionFunctions#number}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> value() {
        return VALUE;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> VALUE = NumberExpressionFunctions.<SpreadsheetExpressionEvaluationContext>number()
            .setName(functionName("value"));

    /**
     * {@see DateTimeExpressionFunctions#weekDay}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> weekDay() {
        return fixName(
                DateTimeExpressionFunctions.weekday()
        );
    }

    /**
     * {@see DateTimeExpressionFunctions#weekNum}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> weekNum() {
        return fixName(
                DateTimeExpressionFunctions.weekNum()
        );
    }

    /**
     * {@see DateTimeExpressionFunctions#year}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> year() {
        return fixName(
                DateTimeExpressionFunctions.year()
        );
    }

    /**
     * {@see BooleanExpressionFunctions#xor}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> xor() {
        return fixName(
                BooleanExpressionFunctions.xor()
        );
    }

    private static <T> ExpressionFunction<T, SpreadsheetExpressionEvaluationContext> unformattedNumber(final ExpressionFunction<T, SpreadsheetExpressionEvaluationContext> function,
                                                                                                       final String name) {
        return SpreadsheetExpressionFunctionUnformattedNumber.with(
                function.setName(
                        functionName(name)
                )
        );
    }

    private static boolean filterNonNullAndNotMissingCell(final Object value,
                                                          final SpreadsheetExpressionEvaluationContext context) {
        return null != value &&
                false == (value instanceof SpreadsheetError && ((SpreadsheetError) value).isMissingCell());
    }

    private static Optional<ExpressionFunctionName> functionName(final String name) {
        return Optional.of(
                walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.name(name)
        );
    }

    private static <T> ExpressionFunction<T, SpreadsheetExpressionEvaluationContext> fixName(final ExpressionFunction<T, SpreadsheetExpressionEvaluationContext> function) {
        return function.setName(
                function.name()
                        .map(f -> f.setCaseSensitivity(walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY))
        );
    }

    /**
     * Stops creation
     */
    private SpreadsheetExpressionFunctions() {
        throw new UnsupportedOperationException();
    }
}

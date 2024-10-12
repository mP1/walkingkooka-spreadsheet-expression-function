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
import walkingkooka.collect.set.Sets;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProvider;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProviders;
import walkingkooka.tree.expression.function.stat.StatExpressionFunctions;
import walkingkooka.tree.expression.function.string.StringExpressionFunctions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Collection of static factory methods for numerous {@link ExpressionFunction}.
 */
public final class SpreadsheetExpressionFunctions implements PublicStaticHelper {

    /**
     * An {@link ExpressionFunctionProvider} with all the functions in this project.
     */
    public static ExpressionFunctionProvider expressionFunctionProvider() {
        return ExpressionFunctionProviders.basic(
                Url.parseAbsolute("https://github.com/mP1/walkingkooka-spreadsheet-expression-function/"),
                CaseSensitivity.SENSITIVE,
                Cast.to(
                        Sets.of(
                                address(),
                                average(),
                                averageIf(),
                                cell(),
                                cellValue(),
                                column(),
                                columns(),
                                concat(),
                                count(),
                                countIf(),
                                errorType(),
                                formulaText(),
                                hyperlink(),
                                indirect(),
                                isBlank(),
                                isErr(),
                                isError(),
                                isFormula(),
                                isNa(),
                                isRef(),
                                lambda(),
                                len(),
                                lower(),
                                max(),
                                maxIf(),
                                min(),
                                minIf(),
                                na(),
                                numberValue(),
                                offset(),
                                row(),
                                rows(),
                                sum(),
                                sumIf(),
                                type(),
                                upper()
                        )
                )
        );
    }

    /**
     * {@see SpreadsheetExpressionFunctionAddress}
     */
    public static ExpressionFunction<SpreadsheetCellReference, SpreadsheetExpressionEvaluationContext> address() {
        return SpreadsheetExpressionFunctionAddress.INSTANCE;
    }

    /**
     * {@see StatExpressionFunctions#average}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> average() {
        return StatExpressionFunctions.average();
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberIf#averageIf}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> averageIf() {
        return SpreadsheetExpressionFunctionNumberIf.averageIf();
    }

    /**
     * {@see SpreadsheetExpressionFunctionObjectCell}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> cell() {
        return SpreadsheetExpressionFunctionObjectCell.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionObjectCellValue}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> cellValue() {
        return SpreadsheetExpressionFunctionObjectCellValue.INSTANCE;
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
     * Counts the {@link ExpressionNumber} present in the parameter values
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> count() {
        return COUNT;
    }

    private final static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> COUNT = StatExpressionFunctions.<SpreadsheetExpressionEvaluationContext>count()
            .filterParameterValues(SpreadsheetExpressionFunctions::filterNumbers);

    private static boolean filterNumbers(final Object value,
                                         final SpreadsheetExpressionEvaluationContext context) {
        return value instanceof ExpressionNumber |
                value instanceof LocalDate |
                value instanceof LocalDateTime |
                value instanceof LocalTime;
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberIf#countIf}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> countIf() {
        return SpreadsheetExpressionFunctionNumberIf.countIf();
    }

    /**
     * {@see SpreadsheetExpressionFunctionObjectErrorType}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> errorType() {
        return SpreadsheetExpressionFunctionObjectErrorType.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionStringFormulaText}
     */
    public static ExpressionFunction<String, SpreadsheetExpressionEvaluationContext> formulaText() {
        return SpreadsheetExpressionFunctionStringFormulaText.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionHyperlink}
     */
    public static ExpressionFunction<AbsoluteUrl, SpreadsheetExpressionEvaluationContext> hyperlink() {
        return SpreadsheetExpressionFunctionHyperlink.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionIndirect}
     */
    public static ExpressionFunction<SpreadsheetCellReference, SpreadsheetExpressionEvaluationContext> indirect() {
        return SpreadsheetExpressionFunctionIndirect.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionBooleanIsBlank}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isBlank() {
        return SpreadsheetExpressionFunctionBooleanIsBlank.INSTANCE;
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
     * {@see SpreadsheetExpressionFunctionBooleanIsFormula}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isFormula() {
        return SpreadsheetExpressionFunctionBooleanIsFormula.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionBooleanIsErrErrorNa#isNs}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isNa() {
        return SpreadsheetExpressionFunctionBooleanIsErrErrorNa.isNa();
    }

    /**
     * {@see SpreadsheetExpressionFunctionBooleanIsRef}
     */
    public static ExpressionFunction<Boolean, SpreadsheetExpressionEvaluationContext> isRef() {
        return SpreadsheetExpressionFunctionBooleanIsRef.INSTANCE;
    }

    /**
     * {@see LambdaExpressionFunction}
     */
    public static ExpressionFunction<ExpressionFunction<?, SpreadsheetExpressionEvaluationContext>, SpreadsheetExpressionEvaluationContext> lambda() {
        return SpreadsheetExpressionFunctionLambda.INSTANCE;
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
        return StatExpressionFunctions.max();
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberIf.maxIf}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> maxIf() {
        return SpreadsheetExpressionFunctionNumberIf.maxIf();
    }

    /**
     * {@see StatExpressionFunctions#min}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> min() {
        return StatExpressionFunctions.min();
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberIf.minIf}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> minIf() {
        return SpreadsheetExpressionFunctionNumberIf.minIf();
    }

    /**
     * {@see SpreadsheetExpressionFunctionNa}
     */
    public static ExpressionFunction<SpreadsheetError, SpreadsheetExpressionEvaluationContext> na() {
        return SpreadsheetExpressionFunctionNa.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberValue#INSTANCE}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> numberValue() {
        return SpreadsheetExpressionFunctionNumberValue.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionOffset}
     */
    public static ExpressionFunction<SpreadsheetExpressionReference, SpreadsheetExpressionEvaluationContext> offset() {
        return SpreadsheetExpressionFunctionOffset.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberColumnOrRow#ROW}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> row() {
        return SpreadsheetExpressionFunctionNumberColumnOrRow.ROW;
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberColumnsOrRows#ROWS}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> rows() {
        return SpreadsheetExpressionFunctionNumberColumnsOrRows.ROWS;
    }

    /**
     * {@see StatExpressionFunctions#sum}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> sum() {
        return StatExpressionFunctions.sum();
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberIf#sumIf}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> sumIf() {
        return SpreadsheetExpressionFunctionNumberIf.sumIf();
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberType}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> type() {
        return SpreadsheetExpressionFunctionNumberType.INSTANCE;
    }

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
                ExpressionFunctionName.with(name)
        );
    }

    /**
     * Stops creation
     */
    private SpreadsheetExpressionFunctions() {
        throw new UnsupportedOperationException();
    }
}

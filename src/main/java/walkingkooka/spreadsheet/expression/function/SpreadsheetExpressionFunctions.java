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

import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.function.Consumer;

/**
 * Collection of static factory methods for numerous {@link ExpressionFunction}.
 */
public final class SpreadsheetExpressionFunctions implements PublicStaticHelper {

    /**
     * Visit all {@link ExpressionFunction functions}.
     */
    public static void visit(final Consumer<ExpressionFunction<?, ?>> consumer) {
        Lists.of(
                address(),
                cell(),
                column(),
                columns(),
                errorType(),
                formulaText(),
                hyperlink(),
                isBlank(),
                isErr(),
                isError(),
                isFormula(),
                isNa(),
                na(),
                offset(),
                row(),
                rows(),
                type()
        ).forEach(consumer);
    }

    /**
     * {@see SpreadsheetExpressionFunctionAddress}
     */
    public static ExpressionFunction<SpreadsheetCellReference, SpreadsheetExpressionEvaluationContext> address() {
        return SpreadsheetExpressionFunctionAddress.INSTANCE;
    }

    /**
     * {@see SpreadsheetExpressionFunctionObjectCell}
     */
    public static ExpressionFunction<Object, SpreadsheetExpressionEvaluationContext> cell() {
        return SpreadsheetExpressionFunctionObjectCell.INSTANCE;
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
     * {@see SpreadsheetExpressionFunctionNa}
     */
    public static ExpressionFunction<SpreadsheetError, SpreadsheetExpressionEvaluationContext> na() {
        return SpreadsheetExpressionFunctionNa.INSTANCE;
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
     * {@see SpreadsheetExpressionFunctionNumberType}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> type() {
        return SpreadsheetExpressionFunctionNumberType.INSTANCE;
    }

    /**
     * Stops creation
     */
    private SpreadsheetExpressionFunctions() {
        throw new UnsupportedOperationException();
    }
}

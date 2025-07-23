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
import walkingkooka.spreadsheet.SpreadsheetErrorKind;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

/**
 * A function that returns the next empty row for a given column. This will be useful when forms attempt to find a row to store a form.
 * <pre>
 * NextEmptyRow("3")
 * </pre>
 */
final class SpreadsheetExpressionFunctionNextEmptyRow extends SpreadsheetExpressionFunction<SpreadsheetRowReference> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionNextEmptyRow INSTANCE = new SpreadsheetExpressionFunctionNextEmptyRow();

    private SpreadsheetExpressionFunctionNextEmptyRow() {
        super("nextEmptyRow");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<SpreadsheetColumnReference> COLUMN = ExpressionFunctionParameterName.with("error")
        .required(SpreadsheetColumnReference.class)
        .setKinds(
            ExpressionFunctionParameterKind.CONVERT_EVALUATE
        );

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        COLUMN
    );

    @Override
    public Class<SpreadsheetRowReference> returnType() {
        return SpreadsheetRowReference.class;
    }

    @Override
    public SpreadsheetRowReference apply(final List<Object> parameters,
                                         final SpreadsheetExpressionEvaluationContext context) {
        final SpreadsheetColumnReference column = COLUMN.getOrFail(parameters, 0);

        return context.nextEmptyRow(column)
            .orElseThrow(() -> SpreadsheetErrorKind.VALUE.setMessage("Column full").exception());
    }
}

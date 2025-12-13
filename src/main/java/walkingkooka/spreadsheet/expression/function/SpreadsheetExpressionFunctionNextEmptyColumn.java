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
 * A function that returns the next empty column for a given row. This will be useful when forms attempt to find a column to store a form.
 * <pre>
 * NextEmptyColumn("3")
 * </pre>
 */
final class SpreadsheetExpressionFunctionNextEmptyColumn extends SpreadsheetExpressionFunction<SpreadsheetColumnReference> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionNextEmptyColumn INSTANCE = new SpreadsheetExpressionFunctionNextEmptyColumn();

    private SpreadsheetExpressionFunctionNextEmptyColumn() {
        super("nextEmptyColumn");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<SpreadsheetRowReference> ROW = ExpressionFunctionParameterName.with("error")
        .required(SpreadsheetRowReference.class)
        .setKinds(
            ExpressionFunctionParameterKind.CONVERT_EVALUATE
        );

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        ROW
    );

    @Override
    public Class<SpreadsheetColumnReference> returnType() {
        return SpreadsheetColumnReference.class;
    }

    @Override
    public SpreadsheetColumnReference apply(final List<Object> parameters,
                                            final SpreadsheetExpressionEvaluationContext context) {
        final SpreadsheetRowReference row = ROW.getOrFail(parameters, 0);

        return context.nextEmptyColumn(row)
            .orElseThrow(
                () -> SpreadsheetErrorKind.VALUE.setMessage("Row " + row + " full")
                    .exception()
            );
    }
}

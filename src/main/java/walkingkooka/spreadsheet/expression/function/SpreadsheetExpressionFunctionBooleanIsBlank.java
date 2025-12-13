
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

import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

// https://support.google.com/docs/answer/3093290?hl=en&ref_topic=3105471
// Checks whether the referenced cell is empty. whether a formula is in the referenced cell.
// Only a cell-reference that loads no SpreadsheetCell will return true.
final class SpreadsheetExpressionFunctionBooleanIsBlank extends SpreadsheetExpressionFunctionBoolean {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionBooleanIsBlank INSTANCE = new SpreadsheetExpressionFunctionBooleanIsBlank();

    private SpreadsheetExpressionFunctionBooleanIsBlank() {
        super("isBlank");
    }

    @Override
    public Boolean apply(final List<Object> parameters,
                         final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        final Object maybeReference = REFERENCE.getOrFail(parameters, 0, context);
        boolean blank = false;

        if (maybeReference instanceof SpreadsheetCellReference) {
            blank = !context.loadCell((SpreadsheetCellReference) maybeReference)
                .isPresent();
        }

        return blank;
    }

    final static ExpressionFunctionParameter<Object> REFERENCE = ExpressionFunctionParameterName.with("reference")
        .required(Object.class)
        .setKinds(
            Sets.of(ExpressionFunctionParameterKind.EVALUATE)
        );

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(REFERENCE);
}

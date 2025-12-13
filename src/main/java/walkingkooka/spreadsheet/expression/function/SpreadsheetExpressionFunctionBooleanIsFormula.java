
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

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;
import java.util.Optional;

// https://support.google.com/docs/answer/6270316?hl=en
// Checks whether a formula is in the referenced cell.
final class SpreadsheetExpressionFunctionBooleanIsFormula extends SpreadsheetExpressionFunctionBoolean {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionBooleanIsFormula INSTANCE = new SpreadsheetExpressionFunctionBooleanIsFormula();

    private SpreadsheetExpressionFunctionBooleanIsFormula() {
        super("isFormula");
    }

    @Override
    public Boolean apply(final List<Object> parameters,
                         final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        final SpreadsheetExpressionReference reference = CELL_OR_RANGE_REFERENCE.getOrFail(parameters, 0, context);
        final Optional<SpreadsheetCell> cell = context.loadCell(reference.toCell());
        return cell.isPresent() &&
            cell.get()
                .formula()
                .isNotEmpty();
    }

    final static ExpressionFunctionParameter<SpreadsheetCellReference> REFERENCE = ExpressionFunctionParameterName.with("reference")
        .required(SpreadsheetCellReference.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(REFERENCE);
}

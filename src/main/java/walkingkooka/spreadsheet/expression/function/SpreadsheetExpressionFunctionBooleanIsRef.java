
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

import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;

// https://exceljet.net/excel-functions/excel-isref-function
final class SpreadsheetExpressionFunctionBooleanIsRef extends SpreadsheetExpressionFunctionBoolean {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionBooleanIsRef INSTANCE = new SpreadsheetExpressionFunctionBooleanIsRef();

    private SpreadsheetExpressionFunctionBooleanIsRef() {
        super("isRef");
    }

    @Override
    public Boolean apply(final List<Object> parameters,
                         final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        return CELL_OR_RANGE_REFERENCE.getOrFail(parameters, 0) instanceof SpreadsheetSelection; // lgtm [java/useless-type-test]
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(CELL_OR_RANGE_REFERENCE);

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }
}

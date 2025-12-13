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
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

/**
 * A function that may be used to get the formula text from a {@link SpreadsheetCell}
 */
final class SpreadsheetExpressionFunctionGetFormulaText extends SpreadsheetExpressionFunction<String> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionGetFormulaText INSTANCE = new SpreadsheetExpressionFunctionGetFormulaText();

    private SpreadsheetExpressionFunctionGetFormulaText() {
        super("getFormulaText");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<SpreadsheetCell> CELL = ExpressionFunctionParameterName.with("cell")
        .required(SpreadsheetCell.class)
        .setKinds(
            ExpressionFunctionParameterKind.CONVERT_EVALUATE_RESOLVE_REFERENCES
        );

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(CELL);

    @Override
    public Class<String> returnType() {
        return String.class;
    }

    @Override
    public String apply(final List<Object> parameters,
                        final SpreadsheetExpressionEvaluationContext context) {
        return CELL.getOrFail(parameters, 0)
            .formula()
            .text();
    }
}

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
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

/**
 * A function that may be used to get a {@link DecimalNumberSymbols }.
 */
final class SpreadsheetExpressionFunctionGetDecimalNumberSymbols extends SpreadsheetExpressionFunction<DecimalNumberSymbols> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionGetDecimalNumberSymbols INSTANCE = new SpreadsheetExpressionFunctionGetDecimalNumberSymbols();

    private SpreadsheetExpressionFunctionGetDecimalNumberSymbols() {
        super("getDecimalNumberSymbols");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<DecimalNumberSymbols> DECIMAL_NUMBER_SYMBOLS = ExpressionFunctionParameterName.with("decimalNumberSymbols")
        .required(DecimalNumberSymbols.class)
        .setKinds(
            ExpressionFunctionParameterKind.CONVERT_EVALUATE_RESOLVE_REFERENCES
        );

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        DECIMAL_NUMBER_SYMBOLS
    );

    @Override
    public Class<DecimalNumberSymbols> returnType() {
        return DecimalNumberSymbols.class;
    }

    @Override
    public DecimalNumberSymbols apply(final List<Object> parameters,
                                      final SpreadsheetExpressionEvaluationContext context) {
        return DECIMAL_NUMBER_SYMBOLS.getOrFail(parameters, 0);

    }
}

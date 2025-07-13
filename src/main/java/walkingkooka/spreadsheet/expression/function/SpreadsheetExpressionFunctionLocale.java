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
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;
import java.util.Locale;

/**
 * A function that returns a {@link java.util.Locale}.
 * A converter is expected to handle converting language-tags in String form to a Locale.
 */
final class SpreadsheetExpressionFunctionLocale extends SpreadsheetExpressionFunction<Locale> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionLocale INSTANCE = new SpreadsheetExpressionFunctionLocale();

    private SpreadsheetExpressionFunctionLocale() {
        super("locale");
    }

    @Override
    public Class<Locale> returnType() {
        return Locale.class;
    }

    @Override
    public Locale apply(final List<Object> values,
                        final SpreadsheetExpressionEvaluationContext context) {
        return LOCALE.getOrFail(
                values,
                0
        );
    }

    /**
     * Given the count assembles the parameters with the correct parameter names and types.
     */
    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        List<ExpressionFunctionParameter<?>> parameters;

        switch (count) {
            case 0:
                throw new IllegalArgumentException("Missing locale");
            case 1:
                parameters = PARAMETERS;
                break;
            default:
                throw new IllegalArgumentException("Missing locale");
        }

        return parameters;
    }

    private final ExpressionFunctionParameter<Locale> LOCALE = ExpressionFunctionParameterName.with("locale")
            .required(Locale.class)
            .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(LOCALE);
}

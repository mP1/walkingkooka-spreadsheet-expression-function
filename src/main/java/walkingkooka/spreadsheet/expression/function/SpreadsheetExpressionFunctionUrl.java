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
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;

import java.util.List;

/**
 * Base for any function that returns an {@link walkingkooka.net.Url}
 */
abstract class SpreadsheetExpressionFunctionUrl extends SpreadsheetExpressionFunction<Url> {

    /**
     * Package private ctor
     */
    SpreadsheetExpressionFunctionUrl(final String name) {
        super(name);
    }

    @Override
    public final Url apply(final List<Object> parameters,
                           final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        return this.apply0(
            context.prepareParameters(
                Cast.to(this),
                parameters
            ),
            context
        );
    }

    abstract Url apply0(final List<Object> parameters,
                        final SpreadsheetExpressionEvaluationContext context);

    @Override
    public final Class<Url> returnType() {
        return Url.class;
    }
}

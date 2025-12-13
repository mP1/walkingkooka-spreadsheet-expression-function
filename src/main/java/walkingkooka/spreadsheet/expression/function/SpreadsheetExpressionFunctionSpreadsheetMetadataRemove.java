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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;
import java.util.Objects;

/**
 * A function that removes a value from the given {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata} returning the original value if it was present.
 */
final class SpreadsheetExpressionFunctionSpreadsheetMetadataRemove extends SpreadsheetExpressionFunctionSpreadsheetMetadata<Object> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionSpreadsheetMetadataRemove INSTANCE = new SpreadsheetExpressionFunctionSpreadsheetMetadataRemove();

    /**
     * Private constructor use singleton
     */
    private SpreadsheetExpressionFunctionSpreadsheetMetadataRemove() {
        super("spreadsheetMetadataRemove");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int i) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        PROPERTY_NAME
    );

    @Override
    public Class<Object> returnType() {
        return Object.class;
    }

    @Override
    public Object apply(final List<Object> parameters,
                        final SpreadsheetExpressionEvaluationContext context) {
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(context, "context");

        final SpreadsheetMetadataPropertyName<?> propertyName = PROPERTY_NAME.getOrFail(
            parameters,
            0,
            context
        );

        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();
        final Object removedValue = metadata.get(propertyName)
            .orElse(null);

        context.setSpreadsheetMetadata(
            metadata.remove(propertyName)
        );

        return removedValue;
    }
}

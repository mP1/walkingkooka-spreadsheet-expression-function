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
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;

/**
 * A function that the {@link SpreadsheetMetadata} after setting the given {@link SpreadsheetMetadataPropertyName} and value.
 * If the {@link SpreadsheetMetadata} is missing the current is used.
 */
final class SpreadsheetExpressionFunctionSpreadsheetMetadataValueSet extends SpreadsheetExpressionFunctionSpreadsheetMetadataValue<SpreadsheetMetadata> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionSpreadsheetMetadataValueSet INSTANCE = new SpreadsheetExpressionFunctionSpreadsheetMetadataValueSet();

    /**
     * Private constructor use singleton
     */
    private SpreadsheetExpressionFunctionSpreadsheetMetadataValueSet() {
        super("setSpreadsheetMetadataValue");
    }

    @Override
    public Class<SpreadsheetMetadata> returnType() {
        return SpreadsheetMetadata.class;
    }

    @Override
    List<ExpressionFunctionParameter<?>> parametersWithoutSpreadsheetMetadata() {
        return Lists.of(
            PROPERTY_NAME,
            VALUE
        );
    }

    @Override
    SpreadsheetMetadata applyWithSpreadsheetMetadata(final SpreadsheetMetadata spreadsheetMetadata,
                                                     final int parameterIndexOffset,
                                                     final List<Object> parameters,
                                                     final SpreadsheetExpressionEvaluationContext context) {
        final SpreadsheetMetadataPropertyName<?> propertyName = PROPERTY_NAME.getOrFail(
            parameters,
            parameterIndexOffset + 0
        );

        final Object value = context.convertOrFail(
            VALUE.getOrFail(
                parameters,
                parameterIndexOffset + 1
            ),
            propertyName.type()
        );

        return spreadsheetMetadata.set(
            propertyName,
            Cast.to(value)
        );
    }
}

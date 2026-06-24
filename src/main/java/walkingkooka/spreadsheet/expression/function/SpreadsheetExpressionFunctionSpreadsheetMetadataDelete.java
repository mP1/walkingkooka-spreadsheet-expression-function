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
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;
import java.util.Objects;

/**
 * A function that may be used to delete an existing {@link walkingkooka.spreadsheet.meta.SpreadsheetId}. It always returns
 * null.
 */
final class SpreadsheetExpressionFunctionSpreadsheetMetadataDelete extends SpreadsheetExpressionFunctionSpreadsheetMetadata<Void> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionSpreadsheetMetadataDelete INSTANCE = new SpreadsheetExpressionFunctionSpreadsheetMetadataDelete();

    /**
     * Private constructor use singleton
     */
    private SpreadsheetExpressionFunctionSpreadsheetMetadataDelete() {
        super("deleteSpreadsheetMetadata");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int i) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        SPREADSHEET_ID
    );

    @Override
    public Class<Void> returnType() {
        return Void.class;
    }

    @Override
    public Void apply(final List<Object> parameters,
                      final SpreadsheetExpressionEvaluationContext context) {
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(context, "context");

        final SpreadsheetId spreadsheetId = SPREADSHEET_ID.getOrFail(
            parameters,
            0
        );

        context.deleteMetadata(
            spreadsheetId
        );

        return null;
    }
}

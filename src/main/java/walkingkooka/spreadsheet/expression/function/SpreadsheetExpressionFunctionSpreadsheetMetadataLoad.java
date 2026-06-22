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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;
import java.util.Objects;

/**
 * A function that may be used to load a {@link SpreadsheetMetadata}, returning null if none was found with the given {@link walkingkooka.spreadsheet.meta.SpreadsheetId}.
 */
final class SpreadsheetExpressionFunctionSpreadsheetMetadataLoad extends SpreadsheetExpressionFunctionSpreadsheetMetadata<SpreadsheetMetadata> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionSpreadsheetMetadataLoad INSTANCE = new SpreadsheetExpressionFunctionSpreadsheetMetadataLoad();

    /**
     * Private constructor use singleton
     */
    private SpreadsheetExpressionFunctionSpreadsheetMetadataLoad() {
        super("loadSpreadsheetMetadata");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int i) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        SPREADSHEET_ID
    );

    @Override
    public Class<SpreadsheetMetadata> returnType() {
        return SpreadsheetMetadata.class;
    }

    @Override
    public SpreadsheetMetadata apply(final List<Object> parameters,
                                     final SpreadsheetExpressionEvaluationContext context) {
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(context, "context");

        final SpreadsheetId spreadsheetId = SPREADSHEET_ID.getOrFail(
            parameters,
            0
        );

        return context.loadMetadata(
            spreadsheetId
        ).orElse(null);
    }
}

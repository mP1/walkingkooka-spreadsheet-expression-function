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
import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;
import java.util.Objects;

/**
 * Base class for functions that set/get/remove a {@link SpreadsheetMetadataPropertyName}.
 */
abstract class SpreadsheetExpressionFunctionSpreadsheetMetadataValue extends SpreadsheetExpressionFunctionSpreadsheetMetadata<Object> {

    /**
     * Package private constructor use singleton
     */
    SpreadsheetExpressionFunctionSpreadsheetMetadataValue(final String name) {
        super(name);

        final List<ExpressionFunctionParameter<?>> parametersWithoutSpreadsheetMetadata = this.parametersWithoutSpreadsheetMetadata();
        this.parametersWithoutSpreadsheetMetadata = Lists.immutable(parametersWithoutSpreadsheetMetadata);

        final List<ExpressionFunctionParameter<?>> parametersWithSpreadsheetMetadata = Lists.array();
        parametersWithSpreadsheetMetadata.add(SPREADSHEET_METADATA);
        parametersWithSpreadsheetMetadata.addAll(parametersWithoutSpreadsheetMetadata);

        this.parametersWithSpreadsheetMetadata = Lists.immutable(parametersWithSpreadsheetMetadata);
    }

    @Override
    public final Class<Object> returnType() {
        return Object.class;
    }

    abstract List<ExpressionFunctionParameter<?>> parametersWithoutSpreadsheetMetadata();

    @Override
    public final List<ExpressionFunctionParameter<?>> parameters(final int i) {
        final List<ExpressionFunctionParameter<?>> parametersWithoutSpreadsheetMetadata = this.parametersWithoutSpreadsheetMetadata;
        return i == parametersWithoutSpreadsheetMetadata.size() ?
            parametersWithoutSpreadsheetMetadata :
            this.parametersWithSpreadsheetMetadata;
    }

    private final List<ExpressionFunctionParameter<?>> parametersWithoutSpreadsheetMetadata;

    // constants from sub-classes here to avoid race condition where these constants are null.

    final static ExpressionFunctionParameter<Object> VALUE = ExpressionFunctionParameterName.with("value")
        .required(Object.class)
        .setKinds(
            Sets.of(ExpressionFunctionParameterKind.EVALUATE)
        );

    final static ExpressionFunctionParameter<Object> DEFAULT_VALUE = ExpressionFunctionParameterName.with("defaultValue")
        .required(Object.class)
        .setKinds(
            Sets.of(ExpressionFunctionParameterKind.EVALUATE)
        );

    final static ExpressionFunctionParameter<SpreadsheetMetadata> SPREADSHEET_METADATA = ExpressionFunctionParameterName.with("spreadsheetMetadata")
        .required(SpreadsheetMetadata.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE_RESOLVE_REFERENCES);

    private final List<ExpressionFunctionParameter<?>> parametersWithSpreadsheetMetadata;

    @Override
    public final Object apply(final List<Object> parameters,
                              final SpreadsheetExpressionEvaluationContext context) {
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(context, "context");

        this.checkParameterCount(parameters);

        final boolean missingSpreadsheetMetadata = this.parametersWithoutSpreadsheetMetadata.size() == parameters.size();

        final SpreadsheetMetadata spreadsheetMetadata = missingSpreadsheetMetadata ?
            context.spreadsheetMetadata() :
            SPREADSHEET_METADATA.getOrFail(parameters, 0);

        return this.applyWithSpreadsheetMetadata(
            spreadsheetMetadata,
            missingSpreadsheetMetadata ?
                0 :
                1,
            parameters,
            context
        );
    }

    /**
     * Invokes the function resolving the {@link SpreadsheetMetadata} from parameters or {@link SpreadsheetExpressionEvaluationContext}.
     */
    abstract Object applyWithSpreadsheetMetadata(final SpreadsheetMetadata spreadsheetMetadata,
                                                 final int parameterIndexOffset,
                                                 final List<Object> parameters,
                                                 final SpreadsheetExpressionEvaluationContext context);
}

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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

abstract class SpreadsheetExpressionFunctionSpreadsheetMetadata<T> extends SpreadsheetExpressionFunction<T> {

    SpreadsheetExpressionFunctionSpreadsheetMetadata(final String name) {
        super(name);
    }

    final static ExpressionFunctionParameter<SpreadsheetMetadataPropertyName<?>> PROPERTY_NAME = Cast.to(
            ExpressionFunctionParameterName.with("propertyName")
                    .required(SpreadsheetMetadataPropertyName.class)
                    .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE)
    );
}

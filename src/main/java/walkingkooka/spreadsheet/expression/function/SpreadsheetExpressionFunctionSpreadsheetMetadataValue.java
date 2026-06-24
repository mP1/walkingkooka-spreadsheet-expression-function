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

import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

/**
 * Base class for functions that set/get/remove a {@link SpreadsheetMetadataPropertyName}.
 */
abstract class SpreadsheetExpressionFunctionSpreadsheetMetadataValue extends SpreadsheetExpressionFunctionSpreadsheetMetadata<Object> {

    /**
     * Package private constructor use singleton
     */
    SpreadsheetExpressionFunctionSpreadsheetMetadataValue(final String name) {
        super(name);
    }

    @Override
    public final Class<Object> returnType() {
        return Object.class;
    }
}

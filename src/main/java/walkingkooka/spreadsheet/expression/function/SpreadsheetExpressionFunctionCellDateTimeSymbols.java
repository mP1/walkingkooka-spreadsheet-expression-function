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

import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.spreadsheet.SpreadsheetCell;

/**
 * A custom function that retrieves the value for the current cell. This function exists primarily to support
 * filtering cells using a predicate of the current cell.
 */
final class SpreadsheetExpressionFunctionCellDateTimeSymbols extends SpreadsheetExpressionFunctionCell<DateTimeSymbols> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionCellDateTimeSymbols INSTANCE = new SpreadsheetExpressionFunctionCellDateTimeSymbols();

    private SpreadsheetExpressionFunctionCellDateTimeSymbols() {
        super("cellDateTimeSymbols");
    }

    @Override
    public Class<DateTimeSymbols> returnType() {
        return DateTimeSymbols.class;
    }

    @Override
    DateTimeSymbols extractCellPropertyOrNull(final SpreadsheetCell cell) {
        return cell.dateTimeSymbols()
                .orElse(null);
    }
}

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

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;

/**
 * A custom function that retrieves the {@link SpreadsheetParserSelector} for the current cell.
 * <br>
 * This function exists primarily to support filtering cells using a predicate of the current cell.
 */
final class SpreadsheetExpressionFunctionCellParser extends SpreadsheetExpressionFunctionCell<SpreadsheetParserSelector> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionCellParser INSTANCE = new SpreadsheetExpressionFunctionCellParser();

    private SpreadsheetExpressionFunctionCellParser() {
        super("cellParser");
    }

    @Override
    public Class<SpreadsheetParserSelector> returnType() {
        return SpreadsheetParserSelector.class;
    }

    @Override
    SpreadsheetParserSelector extractCellPropertyOrNull(final SpreadsheetCell cell) {
        return cell.parser()
            .orElse(null);
    }
}

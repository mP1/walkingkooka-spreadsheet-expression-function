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
import walkingkooka.tree.text.TextNode;

/**
 * A custom function that retrieves the current cell formatted value, returning null when missing.
 * This is intended to be used to create a query expression and not actual cell formulas.
 */
final class SpreadsheetExpressionFunctionCellFormattedValue extends SpreadsheetExpressionFunctionCell<TextNode> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionCellFormattedValue INSTANCE = new SpreadsheetExpressionFunctionCellFormattedValue();

    private SpreadsheetExpressionFunctionCellFormattedValue() {
        super("cellFormattedValue");
    }

    @Override
    public Class<TextNode> returnType() {
        return TextNode.class;
    }

    @Override
    TextNode extractCellPropertyOrNull(final SpreadsheetCell cell) {
        return cell.formattedValue()
            .orElse(null);
    }
}

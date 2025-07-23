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

import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

public final class SpreadsheetExpressionFunctionCellStyleTest extends SpreadsheetExpressionFunctionCellTestCase<SpreadsheetExpressionFunctionCellStyle, TextStyle> {

    @Override
    public SpreadsheetExpressionFunctionCellStyle createBiFunction() {
        return SpreadsheetExpressionFunctionCellStyle.INSTANCE;
    }

    @Override
    TextStyle valuePresent() {
        return TextStyle.EMPTY.set(
            TextStylePropertyName.TEXT_ALIGN,
            TextAlign.LEFT
        ).set(
            TextStylePropertyName.COLOR,
            Color.parse("#123")
        );
    }

    @Override
    TextStyle valueAbsent() {
        return TextStyle.EMPTY;
    }

    @Override
    SpreadsheetCell setProperty(final SpreadsheetCell cell,
                                final TextStyle value) {
        return cell.setStyle(value);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "cellStyle"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionCellStyle> type() {
        return SpreadsheetExpressionFunctionCellStyle.class;
    }
}

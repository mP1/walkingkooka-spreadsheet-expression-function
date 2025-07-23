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
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;

import java.util.Optional;

public final class SpreadsheetExpressionFunctionCellFormatterTest extends SpreadsheetExpressionFunctionCellTestCase<SpreadsheetExpressionFunctionCellFormatter, SpreadsheetFormatterSelector> {

    @Override
    public SpreadsheetExpressionFunctionCellFormatter createBiFunction() {
        return SpreadsheetExpressionFunctionCellFormatter.INSTANCE;
    }

    @Override
    SpreadsheetFormatterSelector valuePresent() {
        return SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/ddd")
            .spreadsheetFormatterSelector();
    }

    @Override
    SpreadsheetFormatterSelector valueAbsent() {
        return null;
    }

    @Override
    SpreadsheetCell setProperty(final SpreadsheetCell cell,
                                final SpreadsheetFormatterSelector value) {
        return cell.setFormatter(
            Optional.ofNullable(value)
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "cellFormatter"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionCellFormatter> type() {
        return SpreadsheetExpressionFunctionCellFormatter.class;
    }
}

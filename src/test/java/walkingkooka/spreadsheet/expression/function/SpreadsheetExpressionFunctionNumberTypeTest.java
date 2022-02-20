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
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.SpreadsheetErrorKind;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class SpreadsheetExpressionFunctionNumberTypeTest extends SpreadsheetExpressionFunctionNumberTestCase<SpreadsheetExpressionFunctionNumberType> {


    private final static SpreadsheetCellReference B5 = SpreadsheetSelection.parseCell("B5");

    @Test
    public void testNull() {
        this.typeAndCheck(
                null,
                1
        );
    }

    @Test
    public void testNumber() {
        this.typeAndCheck(
                KIND.create(123),
                1
        );
    }

    @Test
    public void testLocalDate() {
        this.typeAndCheck(
                LocalDate.now(),
                1
        );
    }

    @Test
    public void testLocalDateTime() {
        this.typeAndCheck(
                LocalDateTime.now(),
                1
        );
    }

    @Test
    public void testLocalTime() {
        this.typeAndCheck(
                LocalTime.now(),
                1
        );
    }

    @Test
    public void testCell() {
        this.typeAndCheck(
                SpreadsheetSelection.parseCell("A1"),
                1
        );
    }

    @Test
    public void testCharacter() {
        this.typeAndCheck(
                'A',
                2
        );
    }

    @Test
    public void testString() {
        this.typeAndCheck(
                "Abc",
                2
        );
    }

    @Test
    public void testBooleanTrue() {
        this.typeAndCheck(
                true,
                4
        );
    }

    @Test
    public void testBooleanFalse() {
        this.typeAndCheck(
                false,
                4
        );
    }

    @Test
    public void testError() {
        for (final SpreadsheetErrorKind error : SpreadsheetErrorKind.values()) {
            this.typeAndCheck(
                    error.setMessage("message 123"),
                    16
            );
        }
    }

    @Test
    public void testCellRange() {
        this.typeAndCheck(
                SpreadsheetSelection.parseCellRange("A1:A1"),
                64
        );
    }

    @Test
    public void testCellRange2() {
        this.typeAndCheck(
                SpreadsheetSelection.parseCellRange("A1:Z99"),
                64
        );
    }

    @Test
    public void testList() {
        this.typeAndCheck(
                Lists.empty(),
                64
        );
    }

    private void typeAndCheck(final Object value,
                              final int expected) {
        this.applyAndCheck2(
                Lists.of(value),
                KIND.create(expected)
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                SpreadsheetExpressionFunctionNumberType.INSTANCE,
                "type"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionNumberType createBiFunction() {
        return SpreadsheetExpressionFunctionNumberType.INSTANCE;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionNumberType> type() {
        return SpreadsheetExpressionFunctionNumberType.class;
    }
}

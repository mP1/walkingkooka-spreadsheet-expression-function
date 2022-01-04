/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionCellTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionCell, Object> {

    final static SpreadsheetCellReference REFERENCE2 = SpreadsheetSelection.parseCell("E5");

    final static SpreadsheetCellRange RANGE = SpreadsheetSelection.parseCellRange("B2:C3");

    @Test
    public void testUnknownTypeFail() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.apply2("???")
        );
    }

    @Test
    public void testColorFail() {
        this.notYetImplemented("color");
    }

    @Test
    public void testContentsFail() {
        this.notYetImplemented("contents");
    }

    @Test
    public void testFormatFail() {
        this.notYetImplemented("format");
    }

    @Test
    public void testParethesisFail() {
        this.notYetImplemented("parenthesis");
    }

    @Test
    public void testPrefixFail() {
        this.notYetImplemented("prefix");
    }

    @Test
    public void testProtectFail() {
        this.notYetImplemented("protect");
    }

    @Test
    public void testTypeFail() {
        this.notYetImplemented("type");
    }

    @Test
    public void testWidthFail() {
        this.notYetImplemented("width");
    }

    private void notYetImplemented(final Object... parameters) {
        assertThrows(
                UnsupportedOperationException.class,
                () -> this.apply2(parameters)
        );
    }

    @Test
    public void testAddressIncludesCellReferenceParameter() {
        this.applyAndCheck3(
                "address",
                REFERENCE2,
                REFERENCE2
        );
    }

    @Test
    public void testAddressIncludesCellRangeParameter() {
        this.applyAndCheck3(
                "address",
                RANGE,
                SpreadsheetSelection.parseCell("B2")
        );
    }

    @Test
    public void testAddressMissingReferenceParameter() {
        this.applyAndCheck3(
                "address",
                REFERENCE
        );
    }

    @Test
    public void testColIncludesCellReferenceParameter() {
        this.applyAndCheck3(
                "col",
                REFERENCE2,
                KIND.create(
                        REFERENCE2.column()
                                .value()
                )
        );
    }

    @Test
    public void testColIncludesRangeParameter() {
        this.applyAndCheck3(
                "col",
                RANGE,
                KIND.create(
                        RANGE.begin()
                                .column()
                                .value()
                )
        );
    }

    @Test
    public void testColMissingReferenceParameter() {
        this.applyAndCheck3(
                "col",
                KIND.create(
                        REFERENCE.column()
                                .value()
                )
        );
    }

    @Test
    public void testFilename() {
        this.applyAndCheck3(
                "filename",
                ""
        );
    }

    @Test
    public void testRowIncludesCellReferenceParameter() {
        this.applyAndCheck3(
                "row",
                REFERENCE2,
                KIND.create(
                        REFERENCE2.row()
                                .value()
                )
        );
    }

    @Test
    public void testRowIncludesRangeParameter() {
        this.applyAndCheck3(
                "row",
                RANGE,
                KIND.create(
                        RANGE.begin()
                                .row()
                                .value()
                )
        );
    }

    @Test
    public void testRowMissingReferenceParameter() {
        this.applyAndCheck3(
                "row",
                KIND.create(
                        REFERENCE.row().value()
                )
        );
    }

    private void applyAndCheck3(final String typeInfo,
                                final Object expected) {
        this.applyAndCheck2(
                Lists.of(typeInfo),
                expected
        );
    }

    private void applyAndCheck3(final String typeInfo,
                                final SpreadsheetSelection selection,
                                final Object expected) {
        this.applyAndCheck2(
                Lists.of(typeInfo, selection),
                expected
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "cell");
    }

    @Override
    public SpreadsheetExpressionFunctionCell createBiFunction() {
        return SpreadsheetExpressionFunctionCell.INSTANCE;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionCell> type() {
        return SpreadsheetExpressionFunctionCell.class;
    }

    @Override
    public String typeNamePrefix() {
        return SpreadsheetExpressionFunction.class.getSimpleName();
    }
}

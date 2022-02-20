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
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.function.FakeSpreadsheetExpressionFunctionContext;
import walkingkooka.spreadsheet.function.SpreadsheetExpressionFunctionContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionObjectCellTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionObjectCell, Object> {

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
    public void testFormatFail() {
        this.notYetImplemented("format");
    }

    @Test
    public void testParenthesisFail() {
        this.notYetImplemented("parenthesis");
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
        this.cellAndCheck(
                "address",
                REFERENCE2,
                REFERENCE2
        );
    }

    @Test
    public void testAddressIncludesCellRangeParameter() {
        this.cellAndCheck(
                "address",
                RANGE,
                SpreadsheetSelection.parseCell("B2")
        );
    }

    @Test
    public void testAddressMissingReferenceParameter() {
        this.cellAndCheck(
                "address",
                REFERENCE
        );
    }

    @Test
    public void testColIncludesCellReferenceParameter() {
        this.cellAndCheck(
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
        this.cellAndCheck(
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
        this.cellAndCheck(
                "col",
                KIND.create(
                        REFERENCE.column()
                                .value()
                )
        );
    }

    // contents........................................................................................................

    @Test
    public void testContentsCellNotFound() {
        this.contentsAndCheck(
                REFERENCE,
                null,
                "0"
        );
    }

    @Test
    public void testContentsCellExits() {
        final String contents = "Contents 123";

        this.contentsAndCheck(
                REFERENCE,
                SpreadsheetCell.with(
                        REFERENCE,
                        SpreadsheetFormula.EMPTY.setText("=function()")
                ).setFormatted(
                        Optional.of(TextNode.text(contents))
                ),
                contents
        );
    }

    private void contentsAndCheck(final SpreadsheetCellReference reference,
                                  final SpreadsheetCell cell,
                                  final String expected) {
        this.cellAndCheck(
                "contents",
                reference,
                new FakeSpreadsheetExpressionFunctionContext() {

                    @Override
                    public Optional<SpreadsheetCell> cell() {
                        return createContext().cell();
                    }

                    @Override
                    public Optional<SpreadsheetCell> loadCell(final SpreadsheetCellReference c) {
                        checkEquals(reference, c, "loadCell");
                        return Optional.ofNullable(cell);
                    }

                    public String toString() {
                        return "loadCell " + reference + " -> " + cell;
                    }
                },
                expected
        );
    }

    // filename...........................................................................................................

    @Test
    public void testFilename() {
        this.cellAndCheck(
                "filename",
                ""
        );
    }

    // prefix...........................................................................................................

    @Test
    public void testPrefixMissingText() {
        this.prefixAndCheck(
                SpreadsheetCell.with(
                        REFERENCE,
                        SpreadsheetFormula.EMPTY
                ),
                ""
        );
    }

    @Test
    public void testPrefixMissingTextAlign() {
        this.prefixAndCheck(
                TextStyle.EMPTY,
                ""
        );
    }

    @Test
    public void testPrefixTextAlignLeft() {
        this.prefixAndCheck(
                TextAlign.LEFT,
                "'"
        );
    }

    @Test
    public void testPrefixTextAlignRight() {
        this.prefixAndCheck(
                TextAlign.RIGHT,
                "\""
        );
    }

    @Test
    public void testPrefixTextAlignCenter() {
        this.prefixAndCheck(
                TextAlign.CENTER,
                "^"
        );
    }

    @Test
    public void testPrefixTextAlignJustify() {
        this.prefixAndCheck(
                TextAlign.JUSTIFY,
                ""
        );
    }

    private void prefixAndCheck(final TextAlign textAlign,
                                final String expected) {
        this.prefixAndCheck(
                TextStyle.EMPTY.set(
                        TextStylePropertyName.TEXT_ALIGN, textAlign
                ),
                expected
        );
    }

    private void prefixAndCheck(final TextStyle textStyle,
                                final String expected) {
        this.cellAndCheck(
                "prefix",
                REFERENCE,
                SpreadsheetCell.with(
                        REFERENCE,
                        SpreadsheetFormula.EMPTY.setText("'Hello")
                ).setStyle(
                        textStyle
                ).setFormatted(
                        Optional.of(
                                TextNode.text("Hello")
                        )
                ),
                expected
        );
    }

    private void prefixAndCheck(final SpreadsheetCell cell,
                                final String expected) {
        this.cellAndCheck(
                "prefix",
                cell.reference(),
                cell,
                expected
        );
    }

    // row...........................................................................................................

    @Test
    public void testRowIncludesCellReferenceParameter() {
        this.cellAndCheck(
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
        this.cellAndCheck(
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
        this.cellAndCheck(
                "row",
                KIND.create(
                        REFERENCE.row().value()
                )
        );
    }

    private void cellAndCheck(final String typeInfo,
                              final Object expected) {
        this.applyAndCheck2(
                Lists.of(typeInfo),
                expected
        );
    }

    private void cellAndCheck(final String typeInfo,
                              final SpreadsheetSelection selection,
                              final Object expected) {
        this.cellAndCheck(
                typeInfo,
                selection,
                this.createContext(),
                expected
        );
    }

    private void cellAndCheck(final String typeInfo,
                              final SpreadsheetSelection selection,
                              final SpreadsheetCell cell,
                              final Object expected) {
        this.cellAndCheck(
                typeInfo,
                selection,
                new FakeSpreadsheetExpressionFunctionContext() {
                    @Override
                    public Optional<SpreadsheetCell> cell() {
                        return Optional.of(cell);
                    }

                    @Override
                    public String toString() {
                        return "cell: " + this.cell();
                    }
                },
                expected
        );
    }

    private void cellAndCheck(final String typeInfo,
                              final SpreadsheetSelection selection,
                              final SpreadsheetExpressionFunctionContext context,
                              final Object expected) {
        this.applyAndCheck(
                Lists.of(typeInfo, selection),
                context,
                expected
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "cell");
    }

    @Override
    public SpreadsheetExpressionFunctionObjectCell createBiFunction() {
        return SpreadsheetExpressionFunctionObjectCell.INSTANCE;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionObjectCell> type() {
        return SpreadsheetExpressionFunctionObjectCell.class;
    }

    @Override
    public String typeNamePrefix() {
        return SpreadsheetExpressionFunction.class.getSimpleName();
    }
}

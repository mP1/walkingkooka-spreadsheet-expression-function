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
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionOffsetTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionOffset, SpreadsheetExpressionReference> {

    private final static SpreadsheetCellReference B2 = SpreadsheetSelection.parseCell("B2");

    private static final SpreadsheetCellReference B3 = SpreadsheetSelection.parseCell("B3");

    @Test
    public void testUnknownTypeFail() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.apply2("???")
        );
    }

    @Test
    public void testCellX0Y0() {
        this.offsetAndCheck(
                B2,
                0,
                0,
                B2
        );
    }

    @Test
    public void testCellRangeX1Y1() {
        this.offsetAndCheck(
                B2.toCellRange(),
                1,
                1,
                SpreadsheetSelection.parseCell("C3")
        );
    }

    @Test
    public void testCellRangeXMinus1YMinus1() {
        this.offsetAndCheck(
                B2.toCellRange(),
                -1,
                -1,
                SpreadsheetSelection.parseCell("A1")
        );
    }

    @Test
    public void testCellXY() {
        this.offsetAndCheck(
                B2,
                2,
                3,
                SpreadsheetSelection.parseCell("E4")
        );
    }

    @Test
    public void testCellXY2() {
        this.offsetAndCheck(
                SpreadsheetSelection.parseCell("D3"),
                -1,
                -2,
                B2
        );
    }

    @Test
    public void testCellXYHeight() {
        this.offsetAndCheck(
                B2,
                0,
                0,
                2,
                SpreadsheetSelection.parseCellRange("B2:B3")
        );
    }

    @Test
    public void testCellXYHeight2() {
        this.offsetAndCheck(
                B2,
                1,
                2,
                3,
                SpreadsheetSelection.parseCellRange("D3:D5")
        );
    }

    @Test
    public void testCellXYHeightWidth() {
        this.offsetAndCheck(
                B2,
                1,
                2,
                3,
                4,
                SpreadsheetSelection.parseCellRange("D3:G5")
        );
    }

    // OFFSET(A1,4,2) // returns reference to C5

    @Test
    public void testOffsetA1_4_2() {
        this.offsetAndCheck(
                SpreadsheetSelection.parseCell("A1"),
                4,
                2,
                SpreadsheetSelection.parseCell("C5")
        );
    }

    // =OFFSET(A1,0,2,5,1) // returns reference to C1:C5

    @Test
    public void testOffsetA1_0_2_5_1() {
        this.offsetAndCheck(
                SpreadsheetSelection.parseCell("A1"),
                0,
                2,
                5,
                1,
                SpreadsheetSelection.parseCellRange("C1:C5")
        );
    }

    // OFFSET(B3,3,2) // returns D6

    @Test
    public void testOffsetB3_3_2() {
        this.offsetAndCheck(
                B3,
                3,
                2,
                SpreadsheetSelection.parseCell("D6")
        );
    }

    // =OFFSET(B3,1,3,6) // returns E4:E9

    @Test
    public void testOffsetB3_1_3_6() {
        this.offsetAndCheck(
                B3,
                1,
                3,
                6,
                SpreadsheetSelection.parseCellRange("E4:E9")
        );
    }

    // =OFFSET(B3,5,1,1,4) // returns C8:F8

    @Test
    public void testOffsetB3_5_1_1_4() {
        this.offsetAndCheck(
                B3,
                5,
                1,
                1,
                4,
                SpreadsheetSelection.parseCellRange("C8:F8")
        );
    }

    // OFFSET(B3,4,2,3,1) // returns D7:D9

    @Test
    public void testOffsetB3_4_2_3_1() {
        this.offsetAndCheck(
                B3,
                4,
                2,
                3,
                1,
                SpreadsheetSelection.parseCellRange("D7:D9")
        );
    }

    // =OFFSET(B3,4,2,3,2) // returns D7:E9

    @Test
    public void testOffsetB3_4_2_3_2() {
        this.offsetAndCheck(
                B3,
                4,
                2,
                3,
                2,
                SpreadsheetSelection.parseCellRange("D7:E9")
        );
    }

    private void offsetAndCheck(final SpreadsheetExpressionReference cellOrRange,
                                final int rows,
                                final int columns,
                                final SpreadsheetExpressionReference expected) {
        this.applyAndCheck2(
                this.parameters2(cellOrRange, rows, columns),
                expected
        );
    }

    private void offsetAndCheck(final SpreadsheetExpressionReference cellOrRange,
                                final int rows,
                                final int columns,
                                final int height,
                                final SpreadsheetExpressionReference expected) {
        this.applyAndCheck2(
                this.parameters2(cellOrRange, rows, columns, height),
                expected
        );
    }

    private void offsetAndCheck(final SpreadsheetExpressionReference cellOrRange,
                                final int rows,
                                final int columns,
                                final int height,
                                final int width,
                                final SpreadsheetExpressionReference expected) {
        this.applyAndCheck2(
                this.parameters2(cellOrRange, rows, columns, height, width),
                expected
        );
    }

    private List<Object> parameters2(final SpreadsheetExpressionReference cellOrRange,
                                     final int... numbers) {
        final List<Object> parameters = Lists.array();
        parameters.add(cellOrRange);

        for (int number : numbers) {
            parameters.add(EXPRESSION_NUMBER_KIND.create(number));
        }

        return parameters;
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "offset");
    }

    @Override
    public SpreadsheetExpressionFunctionOffset createBiFunction() {
        return SpreadsheetExpressionFunctionOffset.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 3;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionOffset> type() {
        return SpreadsheetExpressionFunctionOffset.class;
    }

    @Override
    public String typeNamePrefix() {
        return SpreadsheetExpressionFunction.class.getSimpleName();
    }
}

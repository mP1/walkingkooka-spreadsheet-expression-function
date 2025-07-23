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
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionAddressTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionAddress, SpreadsheetCellReference> {

    @Test
    public void testUnknownTypeFail() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.apply2("???")
        );
    }

    @Test
    public void testRow1Column1() {
        this.addressAndCheck(
            1,
            1,
            "$A$1"
        );
    }

    @Test
    public void testRow1Column1Abs1() {
        this.addressAndCheck(
            1,
            1,
            1,
            "$A$1"
        );
    }

    @Test
    public void testRow1Column1Abs2() {
        this.addressAndCheck(
            1,
            1,
            2,
            "A$1"
        );
    }

    @Test
    public void testRow1Column1Abs3() {
        this.addressAndCheck(
            1,
            1,
            3,
            "$A1"
        );
    }

    @Test
    public void testRow1Column1Abs4() {
        this.addressAndCheck(
            1,
            1,
            4,
            "A1"
        );
    }

    @Test
    public void testRow4Column5() {
        this.addressAndCheck(
            4,
            5,
            "$E$4"
        );
    }

    @Test
    public void testRow4Column5Abs1A1StyleTrue() {
        this.addressAndCheck(
            4,
            5,
            1,
            true,
            "$E$4"
        );
    }

    private void addressAndCheck(final int rowNum,
                                 final int columnNum,
                                 final String expected) {
        this.applyAndCheck2(
            Lists.of(
                EXPRESSION_NUMBER_KIND.create(rowNum),
                EXPRESSION_NUMBER_KIND.create(columnNum)
            ),
            SpreadsheetSelection.parseCell(expected)
        );
    }

    private void addressAndCheck(final int rowNum,
                                 final int columnNum,
                                 final int absNum,
                                 final String expected) {
        this.applyAndCheck2(
            Lists.of(
                EXPRESSION_NUMBER_KIND.create(rowNum),
                EXPRESSION_NUMBER_KIND.create(columnNum),
                EXPRESSION_NUMBER_KIND.create(absNum)
            ),
            SpreadsheetSelection.parseCell(expected)
        );
    }

    private void addressAndCheck(final int rowNum,
                                 final int columnNum,
                                 final int absNum,
                                 final boolean a1style,
                                 final String expected) {
        this.applyAndCheck2(
            Lists.of(
                EXPRESSION_NUMBER_KIND.create(rowNum),
                EXPRESSION_NUMBER_KIND.create(columnNum),
                EXPRESSION_NUMBER_KIND.create(absNum),
                a1style
            ),
            SpreadsheetSelection.parseCell(expected)
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "address");
    }

    @Override
    public SpreadsheetExpressionFunctionAddress createBiFunction() {
        return SpreadsheetExpressionFunctionAddress.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 3;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return this.createContext0();
    }

    @Override
    public Class<SpreadsheetExpressionFunctionAddress> type() {
        return SpreadsheetExpressionFunctionAddress.class;
    }

    @Override
    public String typeNamePrefix() {
        return SpreadsheetExpressionFunction.class.getSimpleName();
    }
}

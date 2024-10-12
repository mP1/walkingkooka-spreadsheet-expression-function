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

import java.util.List;
import java.util.stream.Collectors;

public final class SpreadsheetExpressionFunctionNumberIfTest extends SpreadsheetExpressionFunctionNumberTestCase<SpreadsheetExpressionFunctionNumberIf> {

    // countIf.........................................................................................................

    @Test
    public void testCountIf() {
        this.countIfAndCheck(
                Lists.of(
                        1,
                        2
                ),
                1,
                1
        );
    }

    @Test
    public void testCountIfHalfFiltered() {
        this.countIfAndCheck(
                Lists.of(
                        1,
                        2,
                        100,
                        200
                ),
                "> 80 + 10",
                2
        );
    }

    private void countIfAndCheck(final List<Object> values,
                                 final Object condition,
                                 final Number expected) {
        this.applyIfAndCheck(
                SpreadsheetExpressionFunctionNumberIf.countIf(),
                values,
                condition,
                expected
        );
    }

    // sumIf.........................................................................................................

    @Test
    public void testSumIf() {
        this.sumIfAndCheck(
                Lists.of(
                        1,
                        2
                ),
                1,
                1
        );
    }

    @Test
    public void testSumIfHalfFiltered() {
        this.sumIfAndCheck(
                Lists.of(
                        1,
                        2,
                        100,
                        200
                ),
                "> 80 + 10",
                100 + 200
        );
    }

    private void sumIfAndCheck(final List<Object> values,
                               final Object condition,
                               final Number expected) {
        this.applyIfAndCheck(
                SpreadsheetExpressionFunctionNumberIf.sumIf(),
                values,
                condition,
                expected
        );
    }

    // SpreadsheetExpressionFunctionNumberIf......................................................................................

    private void applyIfAndCheck(final SpreadsheetExpressionFunctionNumberIf function,
                                 final Object value,
                                 final Object condition,
                                 final Number expected) {
        this.applyAndCheck(
                function,
                List.of(
                        value instanceof List ?
                                prepareList((List<?>) value) :
                                wrapIfNumber(value),
                        condition
                ),
                this.createContext(),
                KIND.create(expected)
        );
    }

    private List<Object> prepareList(final List<?> list) {
        return list.stream()
                .map(this::wrapIfNumber)
                .collect(Collectors.toList());
    }

    private Object wrapIfNumber(final Object value) {
        return value instanceof Number ?
                KIND.create((Number) value) :
                value;
    }


    @Override
    public SpreadsheetExpressionFunctionNumberIf createBiFunction() {
        return SpreadsheetExpressionFunctionNumberIf.countIf();
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return this.createContext0();
    }

    @Override
    public Class<SpreadsheetExpressionFunctionNumberIf> type() {
        return SpreadsheetExpressionFunctionNumberIf.class;
    }
}

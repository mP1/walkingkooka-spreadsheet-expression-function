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
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.SpreadsheetErrorKind;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;

public final class SpreadsheetExpressionFunctionNaTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionNa, SpreadsheetError> {

    @Test
    public void testIsErr() {
        this.checkEquals(
            false,
            SpreadsheetExpressionFunctions.isErr()
                .apply(
                    Lists.of(
                        this.apply2()
                    ),
                    this.createContext()
                )
        );
    }

    @Test
    public void testIsNa() {
        this.checkEquals(
            true,
            SpreadsheetExpressionFunctions.isNa()
                .apply(
                    Lists.of(
                        this.apply2()
                    ),
                    this.createContext()
                )
        );
    }

    @Test
    public void testApply() {
        this.applyAndCheck2(
            Lists.empty(),
            SpreadsheetErrorKind.NA.setMessage("NA()")
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "na"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionNa createBiFunction() {
        return SpreadsheetExpressionFunctionNa.INSTANCE;
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
    public Class<SpreadsheetExpressionFunctionNa> type() {
        return SpreadsheetExpressionFunctionNa.class;
    }

    @Override
    public String typeNamePrefix() {
        return SpreadsheetExpressionFunction.class.getSimpleName();
    }
}

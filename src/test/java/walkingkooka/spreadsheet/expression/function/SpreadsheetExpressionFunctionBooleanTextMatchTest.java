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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionBooleanTextMatchTest extends SpreadsheetExpressionFunctionBooleanTestCase<SpreadsheetExpressionFunctionBooleanTextMatch> {

    @Test
    public void testApplyMissingPatternAndValueFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.apply2(
                )
        );
    }

    @Test
    public void testApplyMissingValueFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> this.apply2(
                        "*"
                )
        );
    }

    @Test
    public void testApplyMatch() {
        this.applyAndCheck2(
                SpreadsheetExpressionFunctionBooleanTextMatch.INSTANCE,
                List.of(
                        "*Hello*",
                        "123Hello456"
                ),
                true
        );
    }

    @Test
    public void testApplyMatch2() {
        this.applyAndCheck2(
                SpreadsheetExpressionFunctionBooleanTextMatch.INSTANCE,
                List.of(
                        "Not *Hello*",
                        "123Hello456"
                ),
                true
        );
    }

    @Test
    public void testApplyNotMatch() {
        this.applyAndCheck2(
                SpreadsheetExpressionFunctionBooleanTextMatch.INSTANCE,
                List.of(
                        "*Hello*",
                        "NotMatched"
                ),
                false
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createBiFunction(),
                "textMatch"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionBooleanTextMatch createBiFunction() {
        return SpreadsheetExpressionFunctionBooleanTextMatch.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    // class...........................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionBooleanTextMatch> type() {
        return SpreadsheetExpressionFunctionBooleanTextMatch.class;
    }
}

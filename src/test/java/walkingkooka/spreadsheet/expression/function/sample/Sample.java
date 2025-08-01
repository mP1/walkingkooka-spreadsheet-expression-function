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

package walkingkooka.spreadsheet.expression.function.sample;

import org.junit.jupiter.api.Assertions;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.function.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.expression.ExpressionNumberKind;

public final class Sample {
    public static void main(final String[] args) {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;

        Assertions.assertEquals(
            kind.create(5),
            SpreadsheetExpressionFunctions.column()
                .apply(
                    Lists.of(SpreadsheetSelection.parseCell("E1")),
                    new FakeSpreadsheetExpressionEvaluationContext() {

                        @Override
                        public ExpressionNumberKind expressionNumberKind() {
                            return kind;
                        }
                    }
                )
        );
    }
}

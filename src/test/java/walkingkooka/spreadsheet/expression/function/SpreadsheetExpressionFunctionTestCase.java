
/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.function.FakeSpreadsheetExpressionFunctionContext;
import walkingkooka.spreadsheet.function.SpreadsheetExpressionFunctionContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.function.ExpressionFunctionTesting;

import java.util.Optional;

public abstract class SpreadsheetExpressionFunctionTestCase<F extends SpreadsheetExpressionFunction<T>, T>
        implements ExpressionFunctionTesting<F, T, SpreadsheetExpressionFunctionContext>,
        ClassTesting2<F>,
        TypeNameTesting<F> {

    final static SpreadsheetCellReference REFERENCE = SpreadsheetSelection.parseCell("Z99");

    final static SpreadsheetFormula FORMULA = SpreadsheetFormula.EMPTY.setText("=1+2");

    final static SpreadsheetCell CELL = SpreadsheetCell.with(
            REFERENCE,
            FORMULA
    );

    final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;

    SpreadsheetExpressionFunctionTestCase() {
        super();
    }

    @Test
    public final void testIsPureTrue() {
        this.isPureAndCheck(
                this.createBiFunction(),
                ExpressionEvaluationContexts.fake(),
                true
        );
    }

    @Test
    public final void testResolveReferences() {
        this.resolveReferenceAndCheck(! (this instanceof SpreadsheetExpressionFunctionNumberColumnOrRowTest));
    }

    @Override
    public final SpreadsheetExpressionFunctionContext createContext() {
        return new FakeSpreadsheetExpressionFunctionContext() {
            @Override
            public ExpressionNumberKind expressionNumberKind() {
                return KIND;
            }

            @Override
            public Optional<SpreadsheetCell> cell() {
                return Optional.of(
                    CELL
                );
            }

            @Override
            public String toString() {
                return KIND + " " + CELL;
            }
        };
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }


    @Override
    public final String typeNamePrefix() {
        return SpreadsheetExpressionFunctionNumber.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}

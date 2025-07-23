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
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;

import java.util.Locale;

public final class SpreadsheetExpressionFunctionLocaleTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionLocale, Locale>
    implements ToStringTesting<SpreadsheetExpressionFunctionLocale> {

    @Override
    public void testSetParametersSame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testApplyWithLocale() {
        final Locale locale = Locale.forLanguageTag("en-AU");

        this.applyAndCheck(
            Lists.of(locale),
            locale
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            SpreadsheetExpressionFunctionLocale.INSTANCE,
            "locale"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionLocale createBiFunction() {
        return SpreadsheetExpressionFunctionLocale.INSTANCE;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return this.createContext0();
    }

    @Override
    public Class<SpreadsheetExpressionFunctionLocale> type() {
        return Cast.to(SpreadsheetExpressionFunctionLocale.class);
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }
}

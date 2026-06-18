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
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.convert.SpreadsheetConverterContext;
import walkingkooka.spreadsheet.convert.SpreadsheetConverters;
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.Optional;

public final class SpreadsheetExpressionFunctionUrlSpreadsheetUrlTest extends SpreadsheetExpressionFunctionUrlTestCase<SpreadsheetExpressionFunctionUrlSpreadsheetUrl> {

    @Test
    public void testApplyMissingSpreadsheetId() {
        this.applyAndCheck(
            Lists.empty(),
            Url.parse("#/404")
        );
    }

    @Test
    public void testApplyStringSpreadsheetId() {
        this.applyAndCheck(
            Lists.of(
                "123"
            ),
            Url.parse("#/123")
        );
    }

    @Test
    public void testApplySpreadsheetId() {
        this.applyAndCheck(
            Lists.of(
                SpreadsheetId.parse("123")
            ),
            Url.parse("#/123")
        );
    }

    @Override
    public SpreadsheetExpressionFunctionUrlSpreadsheetUrl createBiFunction() {
        return SpreadsheetExpressionFunctionUrlSpreadsheetUrl.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return new FakeSpreadsheetExpressionEvaluationContext() {

            @Override
            public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                          final Object value) {
                return parameter.convertOrFail(value, this);
            }

            @Override
            public boolean canConvert(final Object value,
                                      final Class<?> type) {
                return this.converter()
                    .canConvert(
                        value,
                        type,
                        this
                    );
            }

            @Override
            public <TT> Either<TT, String> convert(final Object value,
                                                   final Class<TT> target) {
                return this.converter()
                    .convert(
                        value,
                        target,
                        this
                    );
            }

            @Override
            public Converter<SpreadsheetConverterContext> converter() {
                return Converters.collection(
                    Lists.of(
                        Converters.simple(),
                        SpreadsheetConverters.textToSpreadsheetId()
                    )
                );
            }

            @Override
            public Optional<SpreadsheetId> spreadsheetId() {
                return Optional.of(
                    SpreadsheetId.parse("404")
                );
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionUrlSpreadsheetUrl> type() {
        return SpreadsheetExpressionFunctionUrlSpreadsheetUrl.class;
    }
}

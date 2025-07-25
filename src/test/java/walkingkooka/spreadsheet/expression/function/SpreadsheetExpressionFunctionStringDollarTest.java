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
import walkingkooka.spreadsheet.convert.SpreadsheetConverterContext;
import walkingkooka.spreadsheet.convert.SpreadsheetConverters;
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;

public final class SpreadsheetExpressionFunctionStringDollarTest extends SpreadsheetExpressionFunctionStringTestCase<SpreadsheetExpressionFunctionStringDollar> {

    @Test
    public void testMissingDecimals() {
        this.dollarAndCheck(
            123.456,
            "$AUD123*46"
        );
    }

    @Test
    public void testPlus2Decimals() {
        this.dollarAndCheck(
            123.0,
            2,
            "$AUD123*00"
        );
    }

    @Test
    public void testPlus2DecimalsRoundUp() {
        this.dollarAndCheck(
            123.456,
            2,
            "$AUD123*46"
        );
    }

    @Test
    public void testPlus1Decimal() {
        this.dollarAndCheck(
            123.000,
            1,
            "$AUD123*0"
        );
    }

    @Test
    public void testPlus1DecimalRoundUp() {
        this.dollarAndCheck(
            123.456,
            1,
            "$AUD123*5"
        );
    }

    @Test
    public void testZeroDecimals() {
        this.dollarAndCheck(
            123.456,
            0,
            "$AUD123"
        );
    }

    @Test
    public void testMinus1Decimal() {
        this.dollarAndCheck(
            123.456,
            -1,
            "$AUD120"
        );
    }

    @Test
    public void testMinus1DecimalRoundDown() {
        this.dollarAndCheck(
            3,
            -1,
            "$AUD0"
        );
    }

    @Test
    public void testMinus2Decimal() {
        this.dollarAndCheck(
            123.456,
            -2,
            "$AUD100"
        );
    }

    @Test
    public void testMinus2DecimalRoundUp() {
        this.dollarAndCheck(
            567.890,
            -2,
            "$AUD600"
        );
    }

    @Test
    public void testMinus2DecimalRoundDown() {
        this.dollarAndCheck(
            3,
            -2,
            "$AUD0"
        );
    }

    @Test
    public void testMinus4Decimal() {
        this.dollarAndCheck(
            123456,
            -4,
            "$AUD120,000"
        );
    }

    @Test
    public void testMinus4DecimalRoundUp() {
        this.dollarAndCheck(
            567890,
            -4,
            "$AUD570,000"
        );
    }

    @Test
    public void testMinus4DecimalRoundDown() {
        this.dollarAndCheck(
            123,
            -4,
            "$AUD0"
        );
    }

    @Test
    public void testMinusDecimalZerosValue() {
        this.dollarAndCheck(
            123,
            -10,
            "$AUD0"
        );
    }

    private void dollarAndCheck(final Number value,
                                final String expected) {
        this.dollarAndCheck(
            Lists.of(
                EXPRESSION_NUMBER_KIND.create(value)
            ),
            expected
        );
    }

    private void dollarAndCheck(final Number value,
                                final Number decimals,
                                final String expected) {
        this.dollarAndCheck(
            Lists.of(
                EXPRESSION_NUMBER_KIND.create(value),
                EXPRESSION_NUMBER_KIND.create(decimals)
            ),
            expected
        );
    }

    private void dollarAndCheck(final List<Object> parameters,
                                final String expected) {
        this.applyAndCheck2(
            parameters,
            expected
        );
    }

    @Override
    public SpreadsheetExpressionFunctionStringDollar createBiFunction() {
        return SpreadsheetExpressionFunctionStringDollar.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 2;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return new FakeSpreadsheetExpressionEvaluationContext() {

            @Override
            public ExpressionNumberKind expressionNumberKind() {
                return EXPRESSION_NUMBER_KIND;
            }

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
                        Converters.booleanToNumber(),
                        SpreadsheetConverters.textToText(),
                        SpreadsheetConverters.numberToNumber(),
                        Converters.localDateToLocalDateTime(),
                        Converters.localTimeToLocalDateTime()
                    )
                );
            }

            @Override
            public Locale locale() {
                return Locale.forLanguageTag("DE");
            }

            @Override
            public MathContext mathContext() {
                return MathContext.DECIMAL128;
            }

            @Override
            public String currencySymbol() {
                return "$AUD";
            }

            @Override
            public char groupSeparator() {
                return ',';
            }

            @Override
            public char monetaryDecimalSeparator() {
                return '*';
            }

            @Override
            public char negativeSign() {
                return '-';
            }

            @Override
            public char percentSymbol() {
                return '%';
            }

            @Override
            public char positiveSign() {
                return '+';
            }

            @Override
            public char zeroDigit() {
                return '0';
            }
        };
    }

    @Override
    public Class<SpreadsheetExpressionFunctionStringDollar> type() {
        return SpreadsheetExpressionFunctionStringDollar.class;
    }
}

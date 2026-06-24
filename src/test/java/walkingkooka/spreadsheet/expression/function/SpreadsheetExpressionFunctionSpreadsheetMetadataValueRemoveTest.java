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
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Locale;

public final class SpreadsheetExpressionFunctionSpreadsheetMetadataValueRemoveTest extends SpreadsheetExpressionFunctionSpreadsheetMetadataValueTestCase<SpreadsheetExpressionFunctionSpreadsheetMetadataValueRemove, SpreadsheetMetadata>
    implements SpreadsheetMetadataTesting {

    private final static SpreadsheetMetadataPropertyName<Locale> PROPERTY_NAME = SpreadsheetMetadataPropertyName.LOCALE;

    private final static Locale PROPERTY_VALUE = LOCALE;

    @Test
    public void testApplyWithPropertyName() {
        this.metadata = SPREADSHEET_METADATA;

        this.applyAndCheck(
            Lists.of(PROPERTY_NAME),
            this.metadata.remove(PROPERTY_NAME)
        );
    }

    @Test
    public void testApplyWithSpreadsheetMetadataAndPropertyName() {
        final Locale localeRemoved = Locale.forLanguageTag("en-NZ");
        checkNotEquals(
            LOCALE,
            localeRemoved
        );

        this.metadata = SPREADSHEET_METADATA.set(
            SpreadsheetMetadataPropertyName.LOCALE,
            localeRemoved
        );

        this.applyAndCheck(
            Lists.of(
                this.metadata,
                PROPERTY_NAME
            ),
            SPREADSHEET_METADATA.remove(
                SpreadsheetMetadataPropertyName.LOCALE
            )
        );
    }

    @Override
    public SpreadsheetExpressionFunctionSpreadsheetMetadataValueRemove createBiFunction() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataValueRemove.INSTANCE;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return new FakeSpreadsheetExpressionEvaluationContext() {

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetExpressionFunctionSpreadsheetMetadataValueRemoveTest.this.metadata.set(
                    PROPERTY_NAME,
                    PROPERTY_VALUE
                );
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                return this.successfulConversion(
                    target.cast(value),
                    target
                );
            }
        };
    }

    private SpreadsheetMetadata metadata;

    @Override
    public int minimumParameterCount() {
        return 2;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "removeSpreadsheetMetadataValue"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionSpreadsheetMetadataValueRemove> type() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataValueRemove.class;
    }
}

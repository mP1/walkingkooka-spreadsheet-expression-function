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
import walkingkooka.spreadsheet.meta.SpreadsheetName;

import java.util.Locale;

public final class SpreadsheetExpressionFunctionSpreadsheetMetadataValueSetTest extends SpreadsheetExpressionFunctionSpreadsheetMetadataValueTestCase<SpreadsheetExpressionFunctionSpreadsheetMetadataValueSet, SpreadsheetMetadata>
    implements SpreadsheetMetadataTesting {

    final static SpreadsheetMetadataPropertyName<SpreadsheetName> PROPERTY_NAME = SpreadsheetMetadataPropertyName.SPREADSHEET_NAME;
    final static SpreadsheetName PROPERTY_VALUE = SPREADSHEET_NAME;

    @Test
    public void testApplyWithPropertyName() {
        this.metadata = SPREADSHEET_METADATA;

        this.applyAndCheck(
            Lists.of(
                PROPERTY_NAME,
                PROPERTY_VALUE
            ),
            SPREADSHEET_METADATA.set(
                PROPERTY_NAME,
                PROPERTY_VALUE
            )
        );
    }

    @Test
    public void testApplyWithSpreadsheetMetadataAndPropertyName() {
        final SpreadsheetMetadata metadata = SPREADSHEET_METADATA.set(
            SpreadsheetMetadataPropertyName.LOCALE,
            Locale.forLanguageTag("en-NZ")
        );

        this.checkNotEquals(
            metadata,
            SPREADSHEET_METADATA
        );

        final SpreadsheetName spreadsheetName = SpreadsheetName.with("DifferentSpreadsheetName111");
        this.checkNotEquals(
            PROPERTY_VALUE,
            spreadsheetName
        );

        this.applyAndCheck(
            Lists.of(
                metadata,
                PROPERTY_NAME,
                spreadsheetName
            ),
            metadata.set(
                PROPERTY_NAME,
                spreadsheetName
            )
        );
    }

    @Override
    public SpreadsheetExpressionFunctionSpreadsheetMetadataValueSet createBiFunction() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataValueSet.INSTANCE;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return new FakeSpreadsheetExpressionEvaluationContext() {

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetExpressionFunctionSpreadsheetMetadataValueSetTest.this.metadata;
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
            "setSpreadsheetMetadataValue"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionSpreadsheetMetadataValueSet> type() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataValueSet.class;
    }
}

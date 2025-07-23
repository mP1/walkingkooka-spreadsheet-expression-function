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

public final class SpreadsheetExpressionFunctionSpreadsheetMetadataRemoveTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionSpreadsheetMetadataRemove, Object>
    implements SpreadsheetMetadataTesting {

    private final static SpreadsheetMetadataPropertyName<Boolean> PROPERTY_NAME = SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES;

    private final static boolean PROPERTY_VALUE = true;

    @Test
    public void testApply() {
        this.metadata = METADATA_EN_AU;

        this.applyAndCheck(
            Lists.of(
                SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES
            ),
            PROPERTY_VALUE
        );

        this.checkEquals(
            null,
            this.metadata.getIgnoringDefaults(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME)
                .orElse(null)
        );
    }

    @Override
    public SpreadsheetExpressionFunctionSpreadsheetMetadataRemove createBiFunction() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataRemove.INSTANCE;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return new FakeSpreadsheetExpressionEvaluationContext() {

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return METADATA_EN_AU.set(
                    PROPERTY_NAME,
                    PROPERTY_VALUE
                );
            }

            @Override
            public void setSpreadsheetMetadata(final SpreadsheetMetadata spreadsheetMetadata) {
                SpreadsheetExpressionFunctionSpreadsheetMetadataRemoveTest.this.metadata = spreadsheetMetadata;
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
            "spreadsheetMetadataRemove"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionSpreadsheetMetadataRemove> type() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataRemove.class;
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}

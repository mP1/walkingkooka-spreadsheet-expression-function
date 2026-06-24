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
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

public final class SpreadsheetExpressionFunctionSpreadsheetMetadataDeleteTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionSpreadsheetMetadataDelete, Void>
    implements SpreadsheetMetadataTesting {

    private final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);

    @Test
    public void testApply() {
        this.deleted = false;

        this.applyAndCheck(
            Lists.of(SPREADSHEET_ID),
            null
        );

        this.checkEquals(
            true,
            this.deleted,
            "deleted"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionSpreadsheetMetadataDelete createBiFunction() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataDelete.INSTANCE;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return new FakeSpreadsheetExpressionEvaluationContext() {

            @Override
            public void deleteMetadata(final SpreadsheetId spreadsheetId) {
                checkEquals(
                    SpreadsheetExpressionFunctionSpreadsheetMetadataDeleteTest.SPREADSHEET_ID,
                    spreadsheetId,
                    "spreadsheetId"
                );
                SpreadsheetExpressionFunctionSpreadsheetMetadataDeleteTest.this.deleted = true;
            }
        };
    }

    private boolean deleted;

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "deleteSpreadsheetMetadata"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionSpreadsheetMetadataDelete> type() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataDelete.class;
    }
}

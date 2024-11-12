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
import walkingkooka.CanBeEmptyTesting;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.predicate.PredicateTesting2;
import walkingkooka.test.ParseStringTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextMatchTest implements ParseStringTesting<TextMatch>,
        PredicateTesting2<TextMatch, CharSequence>,
        HashCodeEqualsDefinedTesting2<TextMatch>,
        CanBeEmptyTesting {

    // with.............................................................................................................

    @Test
    public void testWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> TextMatch.parse(null)
        );
    }

    // parse............................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParse() {
        this.parseStringAndCheck(
                "*",
                new TextMatch("*")
        );
    }

    @Test
    public void testParseEmpty() {
        this.parseStringAndCheck(
                "",
                new TextMatch("")
        );
    }


    @Override
    public TextMatch parseString(final String text) {
        return TextMatch.parse(text);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
    }

    // isEmpty..........................................................................................................

    @Test
    public void testIsEmptyWhenEmptyString() {
        this.isEmptyAndCheck(
                "",
                true
        );
    }

    @Test
    public void testIsEmptyWhenNotEmptyWhitespaceOnlyString() {
        this.isEmptyAndCheck(
                " ",
                true
        );
    }

    @Test
    public void testIsEmptyWhenNotEmptyWhitespaceOnlyString2() {
        this.isEmptyAndCheck(
                "  ",
                true
        );
    }

    @Test
    public void testIsEmptyWhenOneGlobPattern() {
        this.isEmptyAndCheck(
                "*",
                false
        );
    }

    @Test
    public void testIsEmptyWhenSeveralGlobPatterns() {
        this.isEmptyAndCheck(
                "1 2* ?3",
                false
        );
    }

    private void isEmptyAndCheck(final String text,
                                 final boolean expected) {
        this.isEmptyAndCheck(
                this.parseString(text),
                expected
        );
    }

    // Predicate........................................................................................................

    @Test
    public void testTestWhenEmptyString() {
        this.testFalse(
                TextMatch.parse(""),
                ""
        );
    }

    @Test
    public void testTestStar() {
        this.testTrue(
                "Hello"
        );
    }

    @Test
    public void testTestWithMultiplePatterns() {
        this.testTrue(
                TextMatch.parse("123, hello"),
                "Hello"
        );
    }

    @Test
    public void testTestWithMultiplePatterns2() {
        this.testTrue(
                TextMatch.parse("123, h?l*"),
                "Hello"
        );
    }

    @Test
    public void testTestWithMultiplePatterns3() {
        this.testFalse(
                TextMatch.parse("123, h?l*"),
                "No"
        );
    }


    @Override
    public TextMatch createPredicate() {
        return TextMatch.parse("*");
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
                TextMatch.parse("different")
        );
    }

    @Test
    public void testEqualsCaseUnimportantButDifferent() {
        this.checkEquals(
                TextMatch.parse("*abc"),
                TextMatch.parse("*ABC")
        );
    }

    @Override
    public TextMatch createObject() {
        return TextMatch.parse("*");
    }

    // class............................................................................................................

    @Override
    public Class<TextMatch> type() {
        return TextMatch.class;
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}

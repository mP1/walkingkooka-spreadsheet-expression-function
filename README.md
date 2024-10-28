[![Build Status](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-spreadsheet-expression-function/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka-spreadsheet-expression-function?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-spreadsheet-expression-function.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-spreadsheet-expression-function/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-spreadsheet-expression-function.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-spreadsheet-expression-function/alerts/)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)

# walkingkooka-spreadsheet-expression-function

An assembly of functions that faithfully match their Excel equivalents in terms of functionality and errors.

This includes an assembly of other general purpose `ExpressionFunction(s)` from other repos.

- [boolean](https://github.com/mP1/walkingkooka-tree-expression-function-boolean)
- [datetime](https://github.com/mP1/walkingkooka-tree-expression-function-datetime)
- [engineering](https://github.com/mP1/walkingkooka-tree-expression-function-engineering)
- [net](https://github.com/mP1/walkingkooka-tree-expression-function-net)
- [number](https://github.com/mP1/walkingkooka-tree-expression-function-number)
- [number-trigonometry](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry)
- [stat](https://github.com/mP1/walkingkooka-tree-expression-function-stat)
- [string](https://github.com/mP1/walkingkooka-tree-expression-function-string)

The two links below provide links to available functions in Excel and Sheets

- [Excel](https://support.microsoft.com/en-au/office/excel-functions-alphabetical-b3944572-255d-4efb-bb96-c6d90033e188)
- [Sheets](https://support.google.com/docs/table/25273?hl=en)

Note the list is incomplete when compared to Excel and Sheets and should be considered a work in progress.

- address(rowNum, colNum)
- abs()
- acos()
- address
- and()
- asin()
- atan()
- average()
- averageIf()
- base()
- bin2dec()
- bin2hex()
- bin2oct()
- bitAnd()
- bitOr()
- bitXor()
- ceil()
- cell(typeInfo=address, col, contents, filename, prefix,
  row) [TODO typeinfos](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/issues/26)
- cellFormatter()
- cellParser()
- cellStyle()
- cellValue()
- char()
- choose()
- clean()
- code()
- column()
- columns()
- concat()
- cos()
- count()
- countA()
- countBlank()
- countIf()
- date()
- day()
- days()
- dec2bin()
- dec2hex()
- dec2oct()
- decimal()
- degrees(),
- delta(),
- dollar()
- e(),
- even()
- exact()
- exp()
- false()
- find()
- fixed()
- formulaText(),
- hex2bin()
- hex2dec()
- hex2oct()
- hour()
- int()
- if()
- ifs()
- indirect()
- isBlank(),
- isErr(),
- isError(),
- isEven(),
- isFormula()
- isLogical()
- isNa(),
- isNonText(),
- isNull(),
- isNumber(),
- isOdd()
- isoWeekNum()
- isText(),
- lambda(),
- left(),
- len(),
- let(),
- ln(),
- log(),
- log10(),
- lower(),
- max(),
- maxIf(),
- mid(),
- min(),
- minIf(),
- minutes()
- mod()
- month()
- not()
- now()
- nullValue()
- numberValue()
- oct2bin()
- oct2dec()
- oct2hex()
- odd()
- offset()
- or()
- pi()
- product()
- proper()
- quotient()
- radians()
- rand()
- randBetween()
- replace()
- rept()
- right()
- roman()
- round()
- roundDown()
- roundUp()
- row()
- rows()
- search()
- second()
- sign()
- sin()
- sinh()
- sqrt()
- substitute()
- sum(),
- sumIf(),
- switch()
- t()
- tan()
- tanh()
- text()
- textjoin()
- time()
- trim()
- true()
- trunc()
- type()
- unichar()
- unicode()
- upper()
- value()
- weeknum()
- year()
- xor()

Many more functions are outstanding and remain [TODO](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/issues).
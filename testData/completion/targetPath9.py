jar_library(
    name='bin',
    dependencies=[
        'before'
        'foo/bar/baz:b<caret>'
        'after'
    ]
)
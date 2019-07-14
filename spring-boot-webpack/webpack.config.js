const path = require('path');

module.exports = {
    entry:{
        index : './src/main/resources/static/js/index.js',
    },
    output: {
        path: path.resolve(__dirname, 'src/main/resources/static/js/out'),
        filename: '[name].bundle.js'
    }
};
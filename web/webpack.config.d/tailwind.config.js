const defaultTheme = require('tailwindcss/defaultTheme')

// must be in the jsMain/resource folder
const mainCssFile = 'styles.css';

const tailwind = {
    darkMode: 'class',
    plugins: [
        require('@tailwindcss/forms')
    ],
    variants: {},
    theme: {
        fontFamily: {
            sans: ['Inter var', ...defaultTheme.fontFamily.sans],
        },
        extend: {
            colors: {
                'darkgray': {
                    '50': '#525356',
                    '100': '#48494c',
                    '200': '#3e3f42',
                    '300': '#343538',
                    '400': '#2a2b2e',
                    '500': '#202124',
                    '600': '#16171a',
                    '700': '#0c0d10',
                    '800': '#020306',
                    '900': '#000000'
                },
                'primary': {
                    '50': '#FFF7FF',
                    '100': '#FFEFFF',
                    '200': '#FED7FE',
                    '300': '#FDBFFD',
                    '400': '#FC8EFC',
                    '500': '#FB5EFB',
                    '600': '#E255E2',
                    '700': '#973897',
                    '800': '#712A71',
                    '900': '#4B1C4B',
                },
                'secondary': {
                    '50': '#F6FFFF',
                    '100': '#EEFFFF',
                    '200': '#D4FEFE',
                    '300': '#BBFEFE',
                    '400': '#87FDFD',
                    '500': '#54FCFC',
                    '600': '#4CE3E3',
                    '700': '#329797',
                    '800': '#267171',
                    '900': '#194C4C',
                },
            }
        },
    },
    content: {
        files: [
            '*.{js,html,css}',
            './kotlin/**/*.{js,html,css}'
        ],
        transform: {
            js: (content) => {
                return content.replaceAll(/(\\r)|(\\n)|(\\r\\n)/g,' ')
            }
        }
    },
};


// webpack tailwind css settings
((config) => {
    ((config) => {
        let entry = config.output.path + '/../processedResources/js/main/' + mainCssFile;
        config.entry.main.push(entry);
        config.module.rules.push({
            test: /\.css$/,
            use: [
                {loader: 'style-loader'},
                {loader: 'css-loader'},
                {
                    loader: 'postcss-loader',
                    options: {
                        postcssOptions: {
                            plugins: [
                                require('tailwindcss')({config: tailwind}),
                                require('autoprefixer'),
                                require('cssnano')
                            ]
                        }
                    }
                }
            ]
        });
    })(config);
})(config);
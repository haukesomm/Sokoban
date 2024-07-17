const MiniCssExtractPlugin = require('mini-css-extract-plugin');

const tailwind = {
    plugins: [
        require('@tailwindcss/forms')
    ],
    variants: {},
    theme: {
        extend: {
            colors: {
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
                    '950': '#321232',
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
                'background': {
                    DEFAULT: 'var(--background)',
                    'accent': 'var(--background-accent)',
                    'contrast': 'var(--background-contrast)',
                },
                'foreground': 'var(--foreground)',
            },
            backgroundImage: {
                'marker-light': "linear-gradient(transparent, transparent 42%, theme('colors.primary.200') 0, theme('colors.primary.200') 85%, transparent 0)",
                'marker-dark': "linear-gradient(transparent, transparent 42%, theme('colors.primary.700') 0, theme('colors.primary.700') 85%, transparent 0)",
            },
        },
    },
    content: {
        files: [
            '*.{js,html,css}',
            './kotlin/**/*.{js,html,css}'
        ],
        transform: {
            js: (content) => {
                return content.replaceAll(/(\\r)|(\\n)|(\\r\\n)/g, ' ')
            }
        }
    },
};


// webpack tailwind css settings
((config) => {
    config.entry.main.push('/kotlin/styles.css');
    config.module.rules.push({
        test: /.css$/,
        use: [
            {loader: MiniCssExtractPlugin.loader},
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
    config.plugins.push(
        new MiniCssExtractPlugin({
            filename: 'styles.css'
        })
    );
})(config);
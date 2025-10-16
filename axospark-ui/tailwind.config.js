module.exports = {
    content: [
        "./index.html",
        "./src/**/*.{vue,js,ts,jsx,tsx}",
        "./node_modules/@shadcn/ui/**/*.{js,ts,jsx,tsx,vue}"
    ],
    darkMode: 'class',
    theme: {
        extend: {
            colors: {
                primary: '#ff2d95',
                secondary: '#00d9ff',
                dark: {
                    900: '#0a0a0f',
                    800: '#13131a',
                    700: '#1a1a24',
                }
            }
        },
    },
    plugins: [],
}

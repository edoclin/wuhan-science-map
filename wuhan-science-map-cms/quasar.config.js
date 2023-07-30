const { configure } = require('quasar/wrappers')
const path = require('path')
const pathSrc = path.resolve(__dirname, 'src')

const Icons = require('unplugin-icons/vite')
const IconsResolver = require('unplugin-icons/resolver')
const AutoImport = require('unplugin-auto-import/vite')
const Components = require('unplugin-vue-components/vite')
const { ElementPlusResolver } = require('unplugin-vue-components/resolvers')
const Inspect = require('vite-plugin-inspect')
const { VueAmapResolver } = require('@vuemap/unplugin-resolver')
const viteCompression = require('vite-plugin-compression')
const Pages = require('vite-plugin-pages')
const downloadUrl = ``

module.exports = configure((ctx) => {
  return {
    htmlVariables: {
      title: '武汉科普一张图',
      desc: '武汉科普一张图后台管理系统',
    },
    downloadUrl,
    server: {},
    eslint: {
      warnings: false,
      errors: false
    },

    boot: ['axios', 'element-plus', 'amap', 'quasar'],

    css: ['app.scss'],

    extras: [],

    build: {
      target: {
        browser: ['es2019', 'edge88', 'firefox78', 'chrome87', 'safari13.1'],
        node: 'node16'
      },

      vueRouterMode: 'history',

      extendViteConf (viteConf) {
      },

      viteVuePluginOptions: {
        build: {
          compress: {
            drop_console: true,
            drop_debugger: true,
          },
        }
      },

      vitePlugins: [[AutoImport({
        imports: ['vue'],
        resolvers: [ElementPlusResolver({
          exclude: /^ElAmap[A-Z]*/
        }), IconsResolver({
          prefix: 'icon',
        }), VueAmapResolver()],
        dts: path.resolve(pathSrc, 'auto-imports.d.ts'),
      }),], [Components({
        resolvers: [ElementPlusResolver({
          exclude: /^ElAmap[A-Z]*/
        }), IconsResolver({
          enabledCollections: ['ep'],
        }), VueAmapResolver()],
        dts: path.resolve(pathSrc, 'components.d.ts'),
      }),], [Icons({
        autoInstall: true,
      }),], [Inspect(),], [viteCompression({
        verbose: true,
        disable: false,
        threshold: 10240,
        algorithm: 'gzip',
        ext: '.gz',
      })], [Pages.default({
        importMode: 'async'
      })]]
    },

    devServer: {
      open: false,
    },

    framework: {
      config: {},
      plugins: ['LocalStorage', 'SessionStorage']
    },

    animations: [],

    ssr: {
      pwa: true,
      prodPort: 3000,
      middlewares: ['render' // keep this as last one
      ]
    },

    pwa: {
      workboxMode: 'generateSW', // or 'injectManifest'
      injectPwaMetaTags: true,
      swFilename: 'sw.js',
      manifestFilename: 'manifest.json',
      useCredentialsForManifestTag: false
    },

    cordova: {
    },

    capacitor: {
      hideSplashscreen: true
    },

    bex: {
      contentScripts: ['my-content-script']
    }
  }
})

/*!
 * artplayer.js v5.1.2
 * Github: https://github.com/zhw2590582/ArtPlayer
 * (c) 2017-2024 Harvey Zack
 * Released under the MIT License.
 */
!function (e, t, r, a, i) {
    var o = "undefined" != typeof globalThis ? globalThis : "undefined" != typeof self ? self : "undefined" != typeof window ? window : "undefined" != typeof global ? global : {},
        n = "function" == typeof o[a] && o[a], s = n.cache || {},
        l = "undefined" != typeof module && "function" == typeof module.require && module.require.bind(module);

    function c(t, r) {
        if (!s[t]) {
            if (!e[t]) {
                var i = "function" == typeof o[a] && o[a];
                if (!r && i) return i(t, !0);
                if (n) return n(t, !0);
                if (l && "string" == typeof t) return l(t);
                var u = Error("Cannot find module '" + t + "'");
                throw u.code = "MODULE_NOT_FOUND", u
            }
            d.resolve = function (r) {
                var a = e[t][1][r];
                return null != a ? a : r
            }, d.cache = {};
            var p = s[t] = new c.Module(t);
            e[t][0].call(p.exports, d, p, p.exports, this)
        }
        return s[t].exports;

        function d(e) {
            var t = d.resolve(e);
            return !1 === t ? {} : c(t)
        }
    }

    c.isParcelRequire = !0, c.Module = function (e) {
        this.id = e, this.bundle = c, this.exports = {}
    }, c.modules = e, c.cache = s, c.parent = n, c.register = function (t, r) {
        e[t] = [function (e, t) {
            t.exports = r
        }, {}]
    }, Object.defineProperty(c, "root", {
        get: function () {
            return o[a]
        }
    }), o[a] = c;
    for (var u = 0; u < t.length; u++) c(t[u]);
    if (r) {
        var p = c(r);
        "object" == typeof exports && "undefined" != typeof module ? module.exports = p : "function" == typeof define && define.amd && define(function () {
            return p
        })
    }
}({
    abjMI: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("bundle-text:./style/index.less"), o = a.interopDefault(i), n = e("option-validator"),
            s = a.interopDefault(n), l = e("./utils/emitter"), c = a.interopDefault(l), u = e("./utils"),
            p = e("./scheme"), d = a.interopDefault(p), f = e("./config"), h = a.interopDefault(f), m = e("./template"),
            g = a.interopDefault(m), v = e("./i18n"), y = a.interopDefault(v), b = e("./player"),
            x = a.interopDefault(b), w = e("./control"), j = a.interopDefault(w), k = e("./contextmenu"),
            S = a.interopDefault(k), I = e("./info"), T = a.interopDefault(I), O = e("./subtitle"),
            E = a.interopDefault(O), M = e("./events"), $ = a.interopDefault(M), F = e("./hotkey"),
            C = a.interopDefault(F), H = e("./layer"), D = a.interopDefault(H), B = e("./loading"),
            z = a.interopDefault(B), A = e("./notice"), R = a.interopDefault(A), L = e("./mask"),
            P = a.interopDefault(L), N = e("./icons"), Z = a.interopDefault(N), _ = e("./setting"),
            q = a.interopDefault(_), V = e("./storage"), W = a.interopDefault(V), U = e("./plugins"),
            Y = a.interopDefault(U);
        let K = 0, X = [];

        class G extends c.default {
            constructor(e, t) {
                super(), this.id = ++K;
                let r = u.mergeDeep(G.option, e);
                if (r.container = e.container, this.option = (0, s.default)(r, d.default), this.isLock = !1, this.isReady = !1, this.isFocus = !1, this.isInput = !1, this.isRotate = !1, this.isDestroy = !1, this.template = new g.default(this), this.events = new $.default(this), this.storage = new W.default(this), this.icons = new Z.default(this), this.i18n = new y.default(this), this.notice = new R.default(this), this.player = new x.default(this), this.layers = new D.default(this), this.controls = new j.default(this), this.contextmenu = new S.default(this), this.subtitle = new E.default(this), this.info = new T.default(this), this.loading = new z.default(this), this.hotkey = new C.default(this), this.mask = new P.default(this), this.setting = new q.default(this), this.plugins = new Y.default(this), "function" == typeof t && this.on("ready", () => t.call(this, this)), G.DEBUG) {
                    let e = e => console.log(`[ART.${this.id}] -> ${e}`);
                    e("Version@" + G.version), e("Env@" + G.env), e("Build@" + G.build);
                    for (let t = 0; t < h.default.events.length; t++) this.on("video:" + h.default.events[t], t => e("Event@" + t.type))
                }
                X.push(this)
            }

            static get instances() {
                return X
            }

            static get version() {
                return "5.1.2"
            }

            static get env() {
                return "production"
            }

            static get build() {
                return "2024-05-27 12:43:07"
            }

            static get config() {
                return h.default
            }

            static get utils() {
                return u
            }

            static get scheme() {
                return d.default
            }

            static get Emitter() {
                return c.default
            }

            static get validator() {
                return s.default
            }

            static get kindOf() {
                return s.default.kindOf
            }

            static get html() {
                return g.default.html
            }

            static get option() {
                return {
                    id: "",
                    container: "#artplayer",
                    url: "",
                    poster: "",
                    type: "",
                    theme: "#f00",
                    volume: .7,
                    isLive: !1,
                    muted: !1,
                    autoplay: !1,
                    autoSize: !1,
                    autoMini: !1,
                    loop: !1,
                    flip: !1,
                    playbackRate: !1,
                    aspectRatio: !1,
                    screenshot: !1,
                    setting: !1,
                    hotkey: !0,
                    pip: !1,
                    mutex: !0,
                    backdrop: !0,
                    fullscreen: !1,
                    fullscreenWeb: !1,
                    subtitleOffset: !1,
                    miniProgressBar: !1,
                    useSSR: !1,
                    playsInline: !0,
                    lock: !1,
                    fastForward: !1,
                    autoPlayback: !1,
                    autoOrientation: !1,
                    airplay: !1,
                    layers: [],
                    contextmenu: [],
                    controls: [],
                    settings: [],
                    quality: [],
                    highlight: [],
                    plugins: [],
                    thumbnails: {url: "", number: 60, column: 10, width: 0, height: 0},
                    subtitle: {
                        url: "",
                        type: "",
                        style: {},
                        name: "",
                        escape: !0,
                        encoding: "utf-8",
                        onVttLoad: e => e
                    },
                    moreVideoAttr: {controls: !1, preload: u.isSafari ? "auto" : "metadata"},
                    i18n: {},
                    icons: {},
                    cssVar: {},
                    customType: {},
                    lang: navigator.language.toLowerCase()
                }
            }

            get proxy() {
                return this.events.proxy
            }

            get query() {
                return this.template.query
            }

            get video() {
                return this.template.$video
            }

            destroy(e = !0) {
                this.events.destroy(), this.template.destroy(e), X.splice(X.indexOf(this), 1), this.isDestroy = !0, this.emit("destroy")
            }
        }

        r.default = G, G.DEBUG = !1, G.CONTEXTMENU = !0, G.NOTICE_TIME = 2e3, G.SETTING_WIDTH = 250, G.SETTING_ITEM_WIDTH = 200, G.SETTING_ITEM_HEIGHT = 35, G.RESIZE_TIME = 200, G.SCROLL_TIME = 200, G.SCROLL_GAP = 50, G.AUTO_PLAYBACK_MAX = 10, G.AUTO_PLAYBACK_MIN = 5, G.AUTO_PLAYBACK_TIMEOUT = 3e3, G.RECONNECT_TIME_MAX = 5, G.RECONNECT_SLEEP_TIME = 1e3, G.CONTROL_HIDE_TIME = 3e3, G.DBCLICK_TIME = 300, G.DBCLICK_FULLSCREEN = !0, G.MOBILE_DBCLICK_PLAY = !0, G.MOBILE_CLICK_PLAY = !1, G.AUTO_ORIENTATION_TIME = 200, G.INFO_LOOP_TIME = 1e3, G.FAST_FORWARD_VALUE = 3, G.FAST_FORWARD_TIME = 1e3, G.TOUCH_MOVE_RATIO = .5, G.VOLUME_STEP = .1,
            G.SEEK_STEP = 5,
            G.PLAYBACK_RATE = [.5, 1, 1.5, 2, 2.5, 3],
            G.ASPECT_RATIO = ["default", "4:3", "16:9"],
            G.FLIP = ["normal", "horizontal", "vertical"],
            G.FULLSCREEN_WEB_IN_BODY = !1,
            G.LOG_VERSION = !0, G.USE_RAF = !1, u.isBrowser && (window.Artplayer = G, u.setStyleText("artplayer-style", o.default), setTimeout(() => {
            G.LOG_VERSION && console.log(`%c ArtPlayer %c ${G.version} %c https://artplayer.org`, "color: #fff; background: #5f5f5f", "color: #fff; background: #4bc729", "")
        }, 100))
    }, {
        "bundle-text:./style/index.less": "kfOe8",
        "option-validator": "9I0i9",
        "./utils/emitter": "2bGVu",
        "./utils": "h3rH9",
        "./scheme": "AdvwB",
        "./config": "9Xmqu",
        "./template": "2gKYH",
        "./i18n": "1AdeF",
        "./player": "556MW",
        "./control": "14IBq",
        "./contextmenu": "7iUum",
        "./info": "hD2Lg",
        "./subtitle": "lum0D",
        "./events": "1Epl5",
        "./hotkey": "eTow4",
        "./layer": "4fDoD",
        "./loading": "fE0Sp",
        "./notice": "9PuGy",
        "./mask": "2etr0",
        "./icons": "6dYSr",
        "./setting": "bRHiA",
        "./storage": "f2Thp",
        "./plugins": "96ThS",
        "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"
    }],
    kfOe8: [function (e, t, r) {
        t.exports = '.art-video-player{--art-theme:red;--art-font-color:#fff;--art-background-color:#000;--art-text-shadow-color:#00000080;--art-transition-duration:.2s;--art-padding:10px;--art-border-radius:3px;--art-progress-height:6px;--art-progress-color:#fff3;--art-hover-color:#fff3;--art-loaded-color:#fff3;--art-state-size:80px;--art-state-opacity:.8;--art-bottom-height:100px;--art-bottom-offset:20px;--art-bottom-gap:5px;--art-highlight-width:8px;--art-highlight-color:#ffffff80;--art-control-height:46px;--art-control-opacity:.75;--art-control-icon-size:36px;--art-control-icon-scale:1.1;--art-volume-height:120px;--art-volume-handle-size:14px;--art-lock-size:36px;--art-indicator-scale:0;--art-indicator-size:16px;--art-fullscreen-web-index:9999;--art-settings-icon-size:24px;--art-settings-max-height:300px;--art-selector-max-height:300px;--art-contextmenus-min-width:250px;--art-subtitle-font-size:20px;--art-subtitle-gap:5px;--art-subtitle-bottom:15px;--art-subtitle-border:#000;--art-widget-background:#000000d9;--art-tip-background:#00000080;--art-scrollbar-size:4px;--art-scrollbar-background:#ffffff40;--art-scrollbar-background-hover:#ffffff80;--art-mini-progress-height:2px}.art-bg-cover{background-position:50%;background-repeat:no-repeat;background-size:cover}.art-bottom-gradient{background-image:linear-gradient(#0000,#0006,#000);background-position:bottom;background-repeat:repeat-x}.art-backdrop-filter{backdrop-filter:saturate(180%)blur(20px);background-color:#000000bf!important}.art-truncate{text-overflow:ellipsis;white-space:nowrap;overflow:hidden}.art-video-player{zoom:1;text-align:left;user-select:none;box-sizing:border-box;color:var(--art-font-color);background-color:var(--art-background-color);text-shadow:0 0 2px var(--art-text-shadow-color);-webkit-tap-highlight-color:#0000;-ms-touch-action:manipulation;touch-action:manipulation;-ms-high-contrast-adjust:none;direction:ltr;outline:0;width:100%;height:100%;margin:0 auto;padding:0;font-family:PingFang SC,Helvetica Neue,Microsoft YaHei,Roboto,Arial,sans-serif;font-size:14px;line-height:1.3;position:relative}.art-video-player *,.art-video-player :before,.art-video-player :after{box-sizing:border-box}.art-video-player ::-webkit-scrollbar{width:var(--art-scrollbar-size);height:var(--art-scrollbar-size)}.art-video-player ::-webkit-scrollbar-thumb{background-color:var(--art-scrollbar-background)}.art-video-player ::-webkit-scrollbar-thumb:hover{background-color:var(--art-scrollbar-background-hover)}.art-video-player img{vertical-align:top;max-width:100%}.art-video-player svg{fill:var(--art-font-color)}.art-video-player a{color:var(--art-font-color);text-decoration:none}.art-icon{justify-content:center;align-items:center;line-height:1;display:flex}.art-video-player.art-backdrop .art-contextmenus,.art-video-player.art-backdrop .art-info,.art-video-player.art-backdrop .art-settings,.art-video-player.art-backdrop .art-layer-auto-playback,.art-video-player.art-backdrop .art-selector-list,.art-video-player.art-backdrop .art-volume-inner{backdrop-filter:saturate(180%)blur(20px);background-color:#000000bf!important}.art-video{z-index:10;cursor:pointer;width:100%;height:100%;position:absolute;inset:0}.art-poster{z-index:11;pointer-events:none;background-position:50%;background-repeat:no-repeat;background-size:cover;width:100%;height:100%;position:absolute;inset:0}.art-video-player .art-subtitle{z-index:20;text-align:center;pointer-events:none;justify-content:center;align-items:center;gap:var(--art-subtitle-gap);bottom:var(--art-subtitle-bottom);font-size:var(--art-subtitle-font-size);transition:bottom var(--art-transition-duration)ease;text-shadow:var(--art-subtitle-border)1px 0 1px,var(--art-subtitle-border)0 1px 1px,var(--art-subtitle-border)-1px 0 1px,var(--art-subtitle-border)0 -1px 1px,var(--art-subtitle-border)1px 1px 1px,var(--art-subtitle-border)-1px -1px 1px,var(--art-subtitle-border)1px -1px 1px,var(--art-subtitle-border)-1px 1px 1px;flex-direction:column;width:100%;padding:0 5%;display:none;position:absolute}.art-video-player.art-subtitle-show .art-subtitle{display:flex}.art-video-player.art-control-show .art-subtitle{bottom:calc(var(--art-control-height) + var(--art-subtitle-bottom))}.art-danmuku{z-index:30;pointer-events:none;width:100%;height:100%;position:absolute;inset:0;overflow:hidden}.art-video-player .art-layers{z-index:40;pointer-events:none;width:100%;height:100%;display:none;position:absolute;inset:0}.art-video-player .art-layers .art-layer{pointer-events:auto}.art-video-player.art-layer-show .art-layers{display:flex}.art-video-player .art-mask{z-index:50;pointer-events:none;justify-content:center;align-items:center;width:100%;height:100%;display:flex;position:absolute;inset:0}.art-video-player .art-mask .art-state{opacity:0;width:var(--art-state-size);height:var(--art-state-size);transition:all var(--art-transition-duration)ease;justify-content:center;align-items:center;display:flex;transform:scale(2)}.art-video-player.art-mask-show .art-state{cursor:pointer;pointer-events:auto;opacity:var(--art-state-opacity);transform:scale(1)}.art-video-player.art-loading-show .art-state{display:none}.art-video-player .art-loading{z-index:70;pointer-events:none;justify-content:center;align-items:center;width:100%;height:100%;display:none;position:absolute;inset:0}.art-video-player.art-loading-show .art-loading{display:flex}.art-video-player .art-bottom{z-index:60;opacity:0;pointer-events:none;padding:0 var(--art-padding);transition:all var(--art-transition-duration)ease;background-size:100% var(--art-bottom-height);background-image:linear-gradient(#0000,#0006,#000);background-position:bottom;background-repeat:repeat-x;flex-direction:column;justify-content:flex-end;width:100%;height:100%;display:flex;position:absolute;inset:0;overflow:hidden}.art-video-player .art-bottom .art-controls,.art-video-player .art-bottom .art-progress{transform:translateY(var(--art-bottom-offset));transition:transform var(--art-transition-duration)ease}.art-video-player.art-control-show .art-bottom,.art-video-player.art-hover .art-bottom{opacity:1}.art-video-player.art-control-show .art-bottom .art-controls,.art-video-player.art-hover .art-bottom .art-controls,.art-video-player.art-control-show .art-bottom .art-progress,.art-video-player.art-hover .art-bottom .art-progress{transform:translateY(0)}.art-bottom .art-progress{z-index:0;pointer-events:auto;padding-bottom:var(--art-bottom-gap);position:relative}.art-bottom .art-progress .art-control-progress{cursor:pointer;height:var(--art-progress-height);justify-content:center;align-items:center;display:flex;position:relative}.art-bottom .art-progress .art-control-progress .art-control-progress-inner{transition:height var(--art-transition-duration)ease;background-color:var(--art-progress-color);align-items:center;width:100%;height:50%;display:flex;position:relative}.art-bottom .art-progress .art-control-progress .art-control-progress-inner .art-progress-hover{z-index:0;background-color:var(--art-hover-color);width:0%;height:100%;display:none;position:absolute;inset:0}.art-bottom .art-progress .art-control-progress .art-control-progress-inner .art-progress-loaded{z-index:10;background-color:var(--art-loaded-color);width:0%;height:100%;position:absolute;inset:0}.art-bottom .art-progress .art-control-progress .art-control-progress-inner .art-progress-played{z-index:20;background-color:var(--art-theme);width:0%;height:100%;position:absolute;inset:0}.art-bottom .art-progress .art-control-progress .art-control-progress-inner .art-progress-highlight{z-index:30;pointer-events:none;width:100%;height:100%;position:absolute;inset:0}.art-bottom .art-progress .art-control-progress .art-control-progress-inner .art-progress-highlight span{z-index:0;pointer-events:auto;transform:translateX(calc(var(--art-highlight-width)/-2));background-color:var(--art-highlight-color);width:100%;height:100%;position:absolute;inset:0 auto 0 0;width:var(--art-highlight-width)!important}.art-bottom .art-progress .art-control-progress .art-control-progress-inner .art-progress-indicator{z-index:40;width:var(--art-indicator-size);height:var(--art-indicator-size);transform:scale(var(--art-indicator-scale));margin-left:calc(var(--art-indicator-size)/-2);transition:transform var(--art-transition-duration)ease;border-radius:50%;justify-content:center;align-items:center;display:flex;position:absolute;left:0}.art-bottom .art-progress .art-control-progress .art-control-progress-inner .art-progress-indicator .art-icon{pointer-events:none;width:100%;height:100%}.art-bottom .art-progress .art-control-progress .art-control-progress-inner .art-progress-indicator:hover{transform:scale(1.2)!important}.art-bottom .art-progress .art-control-progress .art-control-progress-inner .art-progress-indicator:active{transform:scale(1)!important}.art-bottom .art-progress .art-control-progress .art-control-progress-inner .art-progress-tip{z-index:50;border-radius:var(--art-border-radius);white-space:nowrap;background-color:var(--art-tip-background);padding:3px 5px;font-size:12px;line-height:1;display:none;position:absolute;top:-25px;left:0}.art-bottom .art-progress .art-control-progress:hover .art-control-progress-inner{height:100%}.art-bottom .art-progress .art-control-thumbnails{bottom:calc(var(--art-bottom-gap) + 10px);border-radius:var(--art-border-radius);pointer-events:none;background-color:var(--art-widget-background);display:none;position:absolute;left:0;box-shadow:0 1px 3px #0003,0 1px 2px -1px #0003}.art-bottom:hover .art-progress .art-control-progress .art-control-progress-inner .art-progress-indicator{transform:scale(1)}.art-controls{z-index:10;pointer-events:auto;height:var(--art-control-height);justify-content:space-between;align-items:center;display:flex;position:relative}.art-controls .art-controls-left,.art-controls .art-controls-right{height:100%;display:flex}.art-controls .art-controls-center{flex:1;justify-content:center;align-items:center;height:100%;padding:0 10px;display:none}.art-controls .art-controls-right{justify-content:flex-end}.art-controls .art-control{cursor:pointer;white-space:nowrap;opacity:var(--art-control-opacity);min-height:var(--art-control-height);min-width:var(--art-control-height);transition:opacity var(--art-transition-duration)ease;flex-shrink:0;justify-content:center;align-items:center;display:flex}.art-controls .art-control .art-icon{height:var(--art-control-icon-size);width:var(--art-control-icon-size);transform:scale(var(--art-control-icon-scale));transition:transform var(--art-transition-duration)ease}.art-controls .art-control .art-icon:active{transform:scale(calc(var(--art-control-icon-scale)*.8))}.art-controls .art-control:hover{opacity:1}.art-control-volume{position:relative}.art-control-volume .art-volume-panel{text-align:center;cursor:default;opacity:0;pointer-events:none;left:0;right:0;bottom:var(--art-control-height);width:var(--art-control-height);height:var(--art-volume-height);transition:all var(--art-transition-duration)ease;justify-content:center;align-items:center;padding:0 5px;font-size:12px;display:flex;position:absolute;transform:translateY(10px)}.art-control-volume .art-volume-panel .art-volume-inner{border-radius:var(--art-border-radius);background-color:var(--art-widget-background);flex-direction:column;align-items:center;gap:10px;width:100%;height:100%;padding:10px 0 12px;display:flex}.art-control-volume .art-volume-panel .art-volume-inner .art-volume-slider{cursor:pointer;flex:1;justify-content:center;width:100%;display:flex;position:relative}.art-control-volume .art-volume-panel .art-volume-inner .art-volume-slider .art-volume-handle{border-radius:var(--art-border-radius);background-color:#ffffff40;justify-content:center;width:2px;display:flex;position:relative;overflow:hidden}.art-control-volume .art-volume-panel .art-volume-inner .art-volume-slider .art-volume-handle .art-volume-loaded{z-index:0;background-color:var(--art-theme);width:100%;height:100%;position:absolute;inset:0}.art-control-volume .art-volume-panel .art-volume-inner .art-volume-slider .art-volume-indicator{width:var(--art-volume-handle-size);height:var(--art-volume-handle-size);margin-top:calc(var(--art-volume-handle-size)/-2);background-color:var(--art-theme);transition:transform var(--art-transition-duration)ease;border-radius:100%;flex-shrink:0;position:absolute;transform:scale(1)}.art-control-volume .art-volume-panel .art-volume-inner .art-volume-slider:active .art-volume-indicator{transform:scale(.9)}.art-control-volume:hover .art-volume-panel{opacity:1;pointer-events:auto;transform:translateY(0)}.art-video-player .art-notice{z-index:80;padding:var(--art-padding);pointer-events:none;width:100%;height:auto;display:none;position:absolute;inset:0 0 auto}.art-video-player .art-notice .art-notice-inner{border-radius:var(--art-border-radius);background-color:var(--art-tip-background);padding:5px;line-height:1;display:inline-flex}.art-video-player.art-notice-show .art-notice{display:flex}.art-video-player .art-contextmenus{z-index:120;border-radius:var(--art-border-radius);background-color:var(--art-widget-background);min-width:var(--art-contextmenus-min-width);flex-direction:column;padding:5px 0;font-size:12px;display:none;position:absolute}.art-video-player .art-contextmenus .art-contextmenu{cursor:pointer;border-bottom:1px solid #ffffff1a;padding:10px 15px;display:flex}.art-video-player .art-contextmenus .art-contextmenu span{padding:0 8px}.art-video-player .art-contextmenus .art-contextmenu span:hover,.art-video-player .art-contextmenus .art-contextmenu span.art-current{color:var(--art-theme)}.art-video-player .art-contextmenus .art-contextmenu:hover{background-color:#ffffff1a}.art-video-player .art-contextmenus .art-contextmenu:last-child{border-bottom:none}.art-video-player.art-contextmenu-show .art-contextmenus{display:flex}.art-video-player .art-settings{z-index:90;border-radius:var(--art-border-radius);transform-origin:100% 100%;max-height:var(--art-settings-max-height);left:auto;right:var(--art-padding);bottom:var(--art-control-height);transform:scale(var(--art-settings-scale));transition:all var(--art-transition-duration)ease;background-color:var(--art-widget-background);flex-direction:column;display:none;position:absolute;overflow:hidden auto}.art-video-player .art-settings .art-setting-panel{flex-direction:column;display:none}.art-video-player .art-settings .art-setting-panel.art-current{display:flex}.art-video-player .art-settings .art-setting-panel .art-setting-item{cursor:pointer;transition:background-color var(--art-transition-duration)ease;justify-content:space-between;align-items:center;padding:0 5px;display:flex;overflow:hidden}.art-video-player .art-settings .art-setting-panel .art-setting-item:hover{background-color:#ffffff1a}.art-video-player .art-settings .art-setting-panel .art-setting-item.art-current{color:var(--art-theme)}.art-video-player .art-settings .art-setting-panel .art-setting-item .art-icon-check{visibility:hidden;height:15px}.art-video-player .art-settings .art-setting-panel .art-setting-item.art-current .art-icon-check{visibility:visible}.art-video-player .art-settings .art-setting-panel .art-setting-item .art-setting-item-left{justify-content:center;align-items:center;gap:5px;display:flex}.art-video-player .art-settings .art-setting-panel .art-setting-item .art-setting-item-left .art-setting-item-left-icon{height:var(--art-settings-icon-size);width:var(--art-settings-icon-size);justify-content:center;align-items:center;display:flex}.art-video-player .art-settings .art-setting-panel .art-setting-item .art-setting-item-right{justify-content:center;align-items:center;gap:5px;font-size:12px;display:flex}.art-video-player .art-settings .art-setting-panel .art-setting-item .art-setting-item-right .art-setting-item-right-tooltip{white-space:nowrap;color:#ffffff80}.art-video-player .art-settings .art-setting-panel .art-setting-item .art-setting-item-right .art-setting-item-right-icon{justify-content:center;align-items:center;min-width:32px;height:24px;display:flex}.art-video-player .art-settings .art-setting-panel .art-setting-item .art-setting-item-right .art-setting-range{appearance:none;background-color:#fff3;outline:none;width:80px;height:3px}.art-video-player .art-settings .art-setting-panel .art-setting-item-back{border-bottom:1px solid #ffffff1a}.art-video-player.art-setting-show .art-settings{display:flex}.art-video-player .art-info{left:var(--art-padding);top:var(--art-padding);z-index:100;border-radius:var(--art-border-radius);background-color:var(--art-widget-background);padding:10px;font-size:12px;display:none;position:absolute}.art-video-player .art-info .art-info-panel{flex-direction:column;gap:5px;display:flex}.art-video-player .art-info .art-info-panel .art-info-item{align-items:center;gap:5px;display:flex}.art-video-player .art-info .art-info-panel .art-info-item .art-info-title{text-align:right;width:100px}.art-video-player .art-info .art-info-panel .art-info-item .art-info-content{text-overflow:ellipsis;white-space:nowrap;user-select:all;width:250px;overflow:hidden}.art-video-player .art-info .art-info-close{cursor:pointer;position:absolute;top:5px;right:5px}.art-video-player.art-info-show .art-info{display:flex}.art-hide-cursor *{cursor:none!important}.art-video-player[data-aspect-ratio]{overflow:hidden}.art-video-player[data-aspect-ratio] .art-video{object-fit:fill;box-sizing:content-box}.art-fullscreen{--art-progress-height:8px;--art-indicator-size:20px;--art-control-height:60px;--art-control-icon-scale:1.3}.art-fullscreen-web{--art-progress-height:8px;--art-indicator-size:20px;--art-control-height:60px;--art-control-icon-scale:1.3;z-index:var(--art-fullscreen-web-index);width:100%;height:100%;position:fixed;inset:0}.art-mini-popup{z-index:9999;border-radius:var(--art-border-radius);cursor:move;user-select:none;background:#000;width:320px;height:180px;transition:opacity .2s;position:fixed;overflow:hidden;box-shadow:0 0 5px #00000080}.art-mini-popup svg{fill:#fff}.art-mini-popup .art-video{pointer-events:none}.art-mini-popup .art-mini-close{z-index:20;cursor:pointer;opacity:0;transition:opacity .2s;position:absolute;top:10px;right:10px}.art-mini-popup .art-mini-state{z-index:30;pointer-events:none;opacity:0;background-color:#00000040;justify-content:center;align-items:center;width:100%;height:100%;transition:opacity .2s;display:flex;position:absolute;inset:0}.art-mini-popup .art-mini-state .art-icon{opacity:.75;cursor:pointer;pointer-events:auto;transition:transform .2s;transform:scale(3)}.art-mini-popup .art-mini-state .art-icon:active{transform:scale(2.5)}.art-mini-popup.art-mini-droging{opacity:.9}.art-mini-popup:hover .art-mini-close,.art-mini-popup:hover .art-mini-state{opacity:1}.art-video-player[data-flip=horizontal] .art-video{transform:scaleX(-1)}.art-video-player[data-flip=vertical] .art-video{transform:scaleY(-1)}.art-video-player .art-layer-lock{height:var(--art-lock-size);width:var(--art-lock-size);top:50%;left:var(--art-padding);background-color:var(--art-tip-background);border-radius:50%;justify-content:center;align-items:center;display:none;position:absolute;transform:translateY(-50%)}.art-video-player .art-layer-auto-playback{border-radius:var(--art-border-radius);left:var(--art-padding);bottom:calc(var(--art-control-height) + var(--art-bottom-gap) + 10px);background-color:var(--art-widget-background);align-items:center;gap:10px;padding:10px;line-height:1;display:none;position:absolute}.art-video-player .art-layer-auto-playback .art-auto-playback-close{cursor:pointer;justify-content:center;align-items:center;display:flex}.art-video-player .art-layer-auto-playback .art-auto-playback-close svg{fill:var(--art-theme);width:15px;height:15px}.art-video-player .art-layer-auto-playback .art-auto-playback-jump{color:var(--art-theme);cursor:pointer}.art-video-player.art-lock .art-subtitle{bottom:var(--art-subtitle-bottom)!important}.art-video-player.art-mini-progress-bar .art-bottom,.art-video-player.art-lock .art-bottom{opacity:1;background-image:none;padding:0}.art-video-player.art-mini-progress-bar .art-bottom .art-controls,.art-video-player.art-lock .art-bottom .art-controls,.art-video-player.art-mini-progress-bar .art-bottom .art-progress,.art-video-player.art-lock .art-bottom .art-progress{transform:translateY(calc(var(--art-control-height) + var(--art-bottom-gap) + var(--art-progress-height)/4))}.art-video-player.art-mini-progress-bar .art-bottom .art-progress-indicator,.art-video-player.art-lock .art-bottom .art-progress-indicator{display:none!important}.art-video-player.art-control-show .art-layer-lock{display:flex}.art-control-selector{position:relative}.art-control-selector .art-selector-list{text-align:center;border-radius:var(--art-border-radius);opacity:0;pointer-events:none;bottom:var(--art-control-height);max-height:var(--art-selector-max-height);background-color:var(--art-widget-background);transition:all var(--art-transition-duration)ease;flex-direction:column;align-items:center;display:flex;position:absolute;overflow:hidden auto;transform:translateY(10px)}.art-control-selector .art-selector-list .art-selector-item{flex-shrink:0;justify-content:center;align-items:center;width:100%;padding:10px 15px;line-height:1;display:flex}.art-control-selector .art-selector-list .art-selector-item:hover{background-color:#ffffff1a}.art-control-selector .art-selector-list .art-selector-item:hover,.art-control-selector .art-selector-list .art-selector-item.art-current{color:var(--art-theme)}.art-control-selector:hover .art-selector-list{opacity:1;pointer-events:auto;transform:translateY(0)}[class*=hint--]{font-style:normal;display:inline-block;position:relative}[class*=hint--]:before,[class*=hint--]:after{visibility:hidden;opacity:0;z-index:1000000;pointer-events:none;transition:all .3s;position:absolute;transform:translate(0,0)}[class*=hint--]:hover:before,[class*=hint--]:hover:after{visibility:visible;opacity:1;transition-delay:.1s}[class*=hint--]:before{content:"";z-index:1000001;background:0 0;border:6px solid #0000;position:absolute}[class*=hint--]:after{color:#fff;white-space:nowrap;background:#000;padding:8px 10px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;font-size:12px;line-height:12px}[class*=hint--][aria-label]:after{content:attr(aria-label)}[class*=hint--][data-hint]:after{content:attr(data-hint)}[aria-label=""]:before,[aria-label=""]:after,[data-hint=""]:before,[data-hint=""]:after{display:none!important}.hint--top-left:before,.hint--top-right:before,.hint--top:before{border-top-color:#000}.hint--bottom-left:before,.hint--bottom-right:before,.hint--bottom:before{border-bottom-color:#000}.hint--left:before{border-left-color:#000}.hint--right:before{border-right-color:#000}.hint--top:before{margin-bottom:-11px}.hint--top:before,.hint--top:after{bottom:100%;left:50%}.hint--top:before{left:calc(50% - 6px)}.hint--top:after{transform:translate(-50%)}.hint--top:hover:before{transform:translateY(-8px)}.hint--top:hover:after{transform:translate(-50%)translateY(-8px)}.hint--bottom:before{margin-top:-11px}.hint--bottom:before,.hint--bottom:after{top:100%;left:50%}.hint--bottom:before{left:calc(50% - 6px)}.hint--bottom:after{transform:translate(-50%)}.hint--bottom:hover:before{transform:translateY(8px)}.hint--bottom:hover:after{transform:translate(-50%)translateY(8px)}.hint--right:before{margin-bottom:-6px;margin-left:-11px}.hint--right:after{margin-bottom:-14px}.hint--right:before,.hint--right:after{bottom:50%;left:100%}.hint--right:hover:before,.hint--right:hover:after{transform:translate(8px)}.hint--left:before{margin-bottom:-6px;margin-right:-11px}.hint--left:after{margin-bottom:-14px}.hint--left:before,.hint--left:after{bottom:50%;right:100%}.hint--left:hover:before,.hint--left:hover:after{transform:translate(-8px)}.hint--top-left:before{margin-bottom:-11px}.hint--top-left:before,.hint--top-left:after{bottom:100%;left:50%}.hint--top-left:before{left:calc(50% - 6px)}.hint--top-left:after{margin-left:12px;transform:translate(-100%)}.hint--top-left:hover:before{transform:translateY(-8px)}.hint--top-left:hover:after{transform:translate(-100%)translateY(-8px)}.hint--top-right:before{margin-bottom:-11px}.hint--top-right:before,.hint--top-right:after{bottom:100%;left:50%}.hint--top-right:before{left:calc(50% - 6px)}.hint--top-right:after{margin-left:-12px;transform:translate(0)}.hint--top-right:hover:before,.hint--top-right:hover:after{transform:translateY(-8px)}.hint--bottom-left:before{margin-top:-11px}.hint--bottom-left:before,.hint--bottom-left:after{top:100%;left:50%}.hint--bottom-left:before{left:calc(50% - 6px)}.hint--bottom-left:after{margin-left:12px;transform:translate(-100%)}.hint--bottom-left:hover:before{transform:translateY(8px)}.hint--bottom-left:hover:after{transform:translate(-100%)translateY(8px)}.hint--bottom-right:before{margin-top:-11px}.hint--bottom-right:before,.hint--bottom-right:after{top:100%;left:50%}.hint--bottom-right:before{left:calc(50% - 6px)}.hint--bottom-right:after{margin-left:-12px;transform:translate(0)}.hint--bottom-right:hover:before,.hint--bottom-right:hover:after{transform:translateY(8px)}.hint--small:after,.hint--medium:after,.hint--large:after{white-space:normal;word-wrap:break-word;line-height:1.4em}.hint--small:after{width:80px}.hint--medium:after{width:150px}.hint--large:after{width:300px}[class*=hint--]:after{text-shadow:0 -1px #000;box-shadow:4px 4px 8px #0000004d}.hint--error:after{text-shadow:0 -1px #592726;background-color:#b34e4d}.hint--error.hint--top-left:before,.hint--error.hint--top-right:before,.hint--error.hint--top:before{border-top-color:#b34e4d}.hint--error.hint--bottom-left:before,.hint--error.hint--bottom-right:before,.hint--error.hint--bottom:before{border-bottom-color:#b34e4d}.hint--error.hint--left:before{border-left-color:#b34e4d}.hint--error.hint--right:before{border-right-color:#b34e4d}.hint--warning:after{text-shadow:0 -1px #6c5328;background-color:#c09854}.hint--warning.hint--top-left:before,.hint--warning.hint--top-right:before,.hint--warning.hint--top:before{border-top-color:#c09854}.hint--warning.hint--bottom-left:before,.hint--warning.hint--bottom-right:before,.hint--warning.hint--bottom:before{border-bottom-color:#c09854}.hint--warning.hint--left:before{border-left-color:#c09854}.hint--warning.hint--right:before{border-right-color:#c09854}.hint--info:after{text-shadow:0 -1px #1a3c4d;background-color:#3986ac}.hint--info.hint--top-left:before,.hint--info.hint--top-right:before,.hint--info.hint--top:before{border-top-color:#3986ac}.hint--info.hint--bottom-left:before,.hint--info.hint--bottom-right:before,.hint--info.hint--bottom:before{border-bottom-color:#3986ac}.hint--info.hint--left:before{border-left-color:#3986ac}.hint--info.hint--right:before{border-right-color:#3986ac}.hint--success:after{text-shadow:0 -1px #1a321a;background-color:#458746}.hint--success.hint--top-left:before,.hint--success.hint--top-right:before,.hint--success.hint--top:before{border-top-color:#458746}.hint--success.hint--bottom-left:before,.hint--success.hint--bottom-right:before,.hint--success.hint--bottom:before{border-bottom-color:#458746}.hint--success.hint--left:before{border-left-color:#458746}.hint--success.hint--right:before{border-right-color:#458746}.hint--always:after,.hint--always:before{opacity:1;visibility:visible}.hint--always.hint--top:before{transform:translateY(-8px)}.hint--always.hint--top:after{transform:translate(-50%)translateY(-8px)}.hint--always.hint--top-left:before{transform:translateY(-8px)}.hint--always.hint--top-left:after{transform:translate(-100%)translateY(-8px)}.hint--always.hint--top-right:before,.hint--always.hint--top-right:after{transform:translateY(-8px)}.hint--always.hint--bottom:before{transform:translateY(8px)}.hint--always.hint--bottom:after{transform:translate(-50%)translateY(8px)}.hint--always.hint--bottom-left:before{transform:translateY(8px)}.hint--always.hint--bottom-left:after{transform:translate(-100%)translateY(8px)}.hint--always.hint--bottom-right:before,.hint--always.hint--bottom-right:after{transform:translateY(8px)}.hint--always.hint--left:before,.hint--always.hint--left:after{transform:translate(-8px)}.hint--always.hint--right:before,.hint--always.hint--right:after{transform:translate(8px)}.hint--rounded:after{border-radius:4px}.hint--no-animate:before,.hint--no-animate:after{transition-duration:0s}.hint--bounce:before,.hint--bounce:after{-webkit-transition:opacity .3s,visibility .3s,-webkit-transform .3s cubic-bezier(.71,1.7,.77,1.24);-moz-transition:opacity .3s,visibility .3s,-moz-transform .3s cubic-bezier(.71,1.7,.77,1.24);transition:opacity .3s,visibility .3s,transform .3s cubic-bezier(.71,1.7,.77,1.24)}.hint--no-shadow:before,.hint--no-shadow:after{text-shadow:initial;box-shadow:initial}.hint--no-arrow:before{display:none}.art-video-player.art-mobile{--art-bottom-gap:10px;--art-control-height:38px;--art-control-icon-scale:1;--art-state-size:60px;--art-settings-max-height:180px;--art-selector-max-height:180px;--art-indicator-scale:1;--art-control-opacity:1}.art-video-player.art-mobile .art-controls-left{margin-left:calc(var(--art-padding)/-1)}.art-video-player.art-mobile .art-controls-right{margin-right:calc(var(--art-padding)/-1)}'
    }, {}],
    "9I0i9": [function (e, t, r) {
        var a;
        a = function () {
            function e(t) {
                return (e = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (e) {
                    return typeof e
                } : function (e) {
                    return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
                })(t)
            }

            var t = Object.prototype.toString, r = function (r) {
                if (void 0 === r) return "undefined";
                if (null === r) return "null";
                var i = e(r);
                if ("boolean" === i) return "boolean";
                if ("string" === i) return "string";
                if ("number" === i) return "number";
                if ("symbol" === i) return "symbol";
                if ("function" === i) return "GeneratorFunction" === a(r) ? "generatorfunction" : "function";
                if (Array.isArray ? Array.isArray(r) : r instanceof Array) return "array";
                if (r.constructor && "function" == typeof r.constructor.isBuffer && r.constructor.isBuffer(r)) return "buffer";
                if (function (e) {
                    try {
                        if ("number" == typeof e.length && "function" == typeof e.callee) return !0
                    } catch (e) {
                        if (-1 !== e.message.indexOf("callee")) return !0
                    }
                    return !1
                }(r)) return "arguments";
                if (r instanceof Date || "function" == typeof r.toDateString && "function" == typeof r.getDate && "function" == typeof r.setDate) return "date";
                if (r instanceof Error || "string" == typeof r.message && r.constructor && "number" == typeof r.constructor.stackTraceLimit) return "error";
                if (r instanceof RegExp || "string" == typeof r.flags && "boolean" == typeof r.ignoreCase && "boolean" == typeof r.multiline && "boolean" == typeof r.global) return "regexp";
                switch (a(r)) {
                    case"Symbol":
                        return "symbol";
                    case"Promise":
                        return "promise";
                    case"WeakMap":
                        return "weakmap";
                    case"WeakSet":
                        return "weakset";
                    case"Map":
                        return "map";
                    case"Set":
                        return "set";
                    case"Int8Array":
                        return "int8array";
                    case"Uint8Array":
                        return "uint8array";
                    case"Uint8ClampedArray":
                        return "uint8clampedarray";
                    case"Int16Array":
                        return "int16array";
                    case"Uint16Array":
                        return "uint16array";
                    case"Int32Array":
                        return "int32array";
                    case"Uint32Array":
                        return "uint32array";
                    case"Float32Array":
                        return "float32array";
                    case"Float64Array":
                        return "float64array"
                }
                if ("function" == typeof r.throw && "function" == typeof r.return && "function" == typeof r.next) return "generator";
                switch (i = t.call(r)) {
                    case"[object Object]":
                        return "object";
                    case"[object Map Iterator]":
                        return "mapiterator";
                    case"[object Set Iterator]":
                        return "setiterator";
                    case"[object String Iterator]":
                        return "stringiterator";
                    case"[object Array Iterator]":
                        return "arrayiterator"
                }
                return i.slice(8, -1).toLowerCase().replace(/\s/g, "")
            };

            function a(e) {
                return e.constructor ? e.constructor.name : null
            }

            function i(e, t) {
                var a = 2 < arguments.length && void 0 !== arguments[2] ? arguments[2] : ["option"];
                return o(e, t, a), n(e, t, a), function (e, t, a) {
                    var s = r(t), l = r(e);
                    if ("object" === s) {
                        if ("object" !== l) throw Error("[Type Error]: '".concat(a.join("."), "' require 'object' type, but got '").concat(l, "'"));
                        Object.keys(t).forEach(function (r) {
                            var s = e[r], l = t[r], c = a.slice();
                            c.push(r), o(s, l, c), n(s, l, c), i(s, l, c)
                        })
                    }
                    if ("array" === s) {
                        if ("array" !== l) throw Error("[Type Error]: '".concat(a.join("."), "' require 'array' type, but got '").concat(l, "'"));
                        e.forEach(function (r, s) {
                            var l = e[s], c = t[s] || t[0], u = a.slice();
                            u.push(s), o(l, c, u), n(l, c, u), i(l, c, u)
                        })
                    }
                }(e, t, a), e
            }

            function o(e, t, a) {
                if ("string" === r(t)) {
                    var i = r(e);
                    if ("?" === t[0] && (t = t.slice(1) + "|undefined"), !(-1 < t.indexOf("|") ? t.split("|").map(function (e) {
                        return e.toLowerCase().trim()
                    }).filter(Boolean).some(function (e) {
                        return i === e
                    }) : t.toLowerCase().trim() === i)) throw Error("[Type Error]: '".concat(a.join("."), "' require '").concat(t, "' type, but got '").concat(i, "'"))
                }
            }

            function n(e, t, a) {
                if ("function" === r(t)) {
                    var i = t(e, r(e), a);
                    if (!0 !== i) {
                        var o = r(i);
                        throw"string" === o ? Error(i) : "error" === o ? i : Error("[Validator Error]: The scheme for '".concat(a.join("."), "' validator require return true, but got '").concat(i, "'"))
                    }
                }
            }

            return i.kindOf = r, i
        }, t.exports = a()
    }, {}],
    "2bGVu": [function (e, t, r) {
        e("@parcel/transformer-js/src/esmodule-helpers.js").defineInteropFlag(r), r.default = class {
            on(e, t, r) {
                let a = this.e || (this.e = {});
                return (a[e] || (a[e] = [])).push({fn: t, ctx: r}), this
            }

            once(e, t, r) {
                let a = this;

                function i(...o) {
                    a.off(e, i), t.apply(r, o)
                }

                return i._ = t, this.on(e, i, r)
            }

            emit(e, ...t) {
                let r = ((this.e || (this.e = {}))[e] || []).slice();
                for (let e = 0; e < r.length; e += 1) r[e].fn.apply(r[e].ctx, t);
                return this
            }

            off(e, t) {
                let r = this.e || (this.e = {}), a = r[e], i = [];
                if (a && t) for (let e = 0, r = a.length; e < r; e += 1) a[e].fn !== t && a[e].fn._ !== t && i.push(a[e]);
                return i.length ? r[e] = i : delete r[e], this
            }
        }
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    guZOB: [function (e, t, r) {
        r.interopDefault = function (e) {
            return e && e.__esModule ? e : {default: e}
        }, r.defineInteropFlag = function (e) {
            Object.defineProperty(e, "__esModule", {value: !0})
        }, r.exportAll = function (e, t) {
            return Object.keys(e).forEach(function (r) {
                "default" === r || "__esModule" === r || Object.prototype.hasOwnProperty.call(t, r) || Object.defineProperty(t, r, {
                    enumerable: !0,
                    get: function () {
                        return e[r]
                    }
                })
            }), t
        }, r.export = function (e, t, r) {
            Object.defineProperty(e, t, {enumerable: !0, get: r})
        }
    }, {}],
    h3rH9: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("./dom");
        a.exportAll(i, r);
        var o = e("./error");
        a.exportAll(o, r);
        var n = e("./subtitle");
        a.exportAll(n, r);
        var s = e("./file");
        a.exportAll(s, r);
        var l = e("./property");
        a.exportAll(l, r);
        var c = e("./time");
        a.exportAll(c, r);
        var u = e("./format");
        a.exportAll(u, r);
        var p = e("./compatibility");
        a.exportAll(p, r)
    }, {
        "./dom": "XgAQE",
        "./error": "2nFlF",
        "./subtitle": "yqFoT",
        "./file": "1VRQn",
        "./property": "3weX2",
        "./time": "7kBIx",
        "./format": "13atT",
        "./compatibility": "luXC1",
        "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"
    }],
    XgAQE: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "query", () => o), a.export(r, "queryAll", () => n), a.export(r, "addClass", () => s), a.export(r, "removeClass", () => l), a.export(r, "hasClass", () => c), a.export(r, "append", () => u), a.export(r, "remove", () => p), a.export(r, "setStyle", () => d), a.export(r, "setStyles", () => f), a.export(r, "getStyle", () => h), a.export(r, "sublings", () => m), a.export(r, "inverseClass", () => g), a.export(r, "tooltip", () => v), a.export(r, "isInViewport", () => y), a.export(r, "includeFromEvent", () => b), a.export(r, "replaceElement", () => x), a.export(r, "createElement", () => w), a.export(r, "getIcon", () => j), a.export(r, "setStyleText", () => k), a.export(r, "supportsFlex", () => S);
        var i = e("./compatibility");

        function o(e, t = document) {
            return t.querySelector(e)
        }

        function n(e, t = document) {
            return Array.from(t.querySelectorAll(e))
        }

        function s(e, t) {
            return e.classList.add(t)
        }

        function l(e, t) {
            return e.classList.remove(t)
        }

        function c(e, t) {
            return e.classList.contains(t)
        }

        function u(e, t) {
            return t instanceof Element ? e.appendChild(t) : e.insertAdjacentHTML("beforeend", String(t)), e.lastElementChild || e.lastChild
        }

        function p(e) {
            return e.parentNode.removeChild(e)
        }

        function d(e, t, r) {
            return e.style[t] = r, e
        }

        function f(e, t) {
            for (let r in t) d(e, r, t[r]);
            return e
        }

        function h(e, t, r = !0) {
            let a = window.getComputedStyle(e, null).getPropertyValue(t);
            return r ? parseFloat(a) : a
        }

        function m(e) {
            return Array.from(e.parentElement.children).filter(t => t !== e)
        }

        function g(e, t) {
            m(e).forEach(e => l(e, t)), s(e, t)
        }

        function v(e, t, r = "top") {
            i.isMobile || (e.setAttribute("aria-label", t), s(e, "hint--rounded"), s(e, `hint--${r}`))
        }

        function y(e, t = 0) {
            let r = e.getBoundingClientRect(), a = window.innerHeight || document.documentElement.clientHeight,
                i = window.innerWidth || document.documentElement.clientWidth,
                o = r.top - t <= a && r.top + r.height + t >= 0, n = r.left - t <= i + t && r.left + r.width + t >= 0;
            return o && n
        }

        function b(e, t) {
            return e.composedPath && e.composedPath().indexOf(t) > -1
        }

        function x(e, t) {
            return t.parentNode.replaceChild(e, t), e
        }

        function w(e) {
            return document.createElement(e)
        }

        function j(e = "", t = "") {
            let r = w("i");
            return s(r, "art-icon"), s(r, `art-icon-${e}`), u(r, t), r
        }

        function k(e, t) {
            let r = document.getElementById(e);
            if (r) r.textContent = t; else {
                let r = w("style");
                r.id = e, r.textContent = t, document.head.appendChild(r)
            }
        }

        function S() {
            let e = document.createElement("div");
            return e.style.display = "flex", "flex" === e.style.display
        }
    }, {"./compatibility": "luXC1", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    luXC1: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "userAgent", () => i), a.export(r, "isSafari", () => o), a.export(r, "isWechat", () => n), a.export(r, "isIE", () => s), a.export(r, "isAndroid", () => l), a.export(r, "isIOS", () => c), a.export(r, "isIOS13", () => u), a.export(r, "isMobile", () => p), a.export(r, "isBrowser", () => d);
        let i = "undefined" != typeof navigator ? navigator.userAgent : "",
            o = /^((?!chrome|android).)*safari/i.test(i), n = /MicroMessenger/i.test(i), s = /MSIE|Trident/i.test(i),
            l = /android/i.test(i), c = /iPad|iPhone|iPod/i.test(i) && !window.MSStream,
            u = c || i.includes("Macintosh") && navigator.maxTouchPoints >= 1,
            p = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(i) || u,
            d = "undefined" != typeof window
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "2nFlF": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "ArtPlayerError", () => i), a.export(r, "errorHandle", () => o);

        class i extends Error {
            constructor(e, t) {
                super(e), "function" == typeof Error.captureStackTrace && Error.captureStackTrace(this, t || this.constructor), this.name = "ArtPlayerError"
            }
        }

        function o(e, t) {
            if (!e) throw new i(t);
            return e
        }
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    yqFoT: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");

        function i(e) {
            return "WEBVTT \r\n\r\n".concat(e.replace(/(\d\d:\d\d:\d\d)[,.](\d+)/g, (e, t, r) => {
                let a = r.slice(0, 3);
                return 1 === r.length && (a = r + "00"), 2 === r.length && (a = r + "0"), `${t},${a}`
            }).replace(/\{\\([ibu])\}/g, "</$1>").replace(/\{\\([ibu])1\}/g, "<$1>").replace(/\{([ibu])\}/g, "<$1>").replace(/\{\/([ibu])\}/g, "</$1>").replace(/(\d\d:\d\d:\d\d),(\d\d\d)/g, "$1.$2").replace(/{[\s\S]*?}/g, "").concat("\r\n\r\n"))
        }

        function o(e) {
            return URL.createObjectURL(new Blob([e], {type: "text/vtt"}))
        }

        function n(e) {
            let t = RegExp("Dialogue:\\s\\d,(\\d+:\\d\\d:\\d\\d.\\d\\d),(\\d+:\\d\\d:\\d\\d.\\d\\d),([^,]*),([^,]*),(?:[^,]*,){4}([\\s\\S]*)$", "i");

            function r(e = "") {
                return e.split(/[:.]/).map((e, t, r) => {
                    if (t === r.length - 1) {
                        if (1 === e.length) return `.${e}00`;
                        if (2 === e.length) return `.${e}0`
                    } else if (1 === e.length) return (0 === t ? "0" : ":0") + e;
                    return 0 === t ? e : t === r.length - 1 ? `.${e}` : `:${e}`
                }).join("")
            }

            return `WEBVTT ${e.split(/\r?\n/).map(e => {
                let a = e.match(t);
                return a ? {
                    start: r(a[1].trim()),
                    end: r(a[2].trim()),
                    text: a[5].replace(/{[\s\S]*?}/g, "").replace(/(\\N)/g, "\n").trim().split(/\r?\n/).map(e => e.trim()).join("\n")
                } : null
            }).filter(e => e).map((e, t) => e ? `${t + 1} ${e.start} --> ${e.end} ${e.text}` : "").filter(e => e.trim()).join("\n\n")}`
        }

        a.defineInteropFlag(r), a.export(r, "srtToVtt", () => i), a.export(r, "vttToBlob", () => o), a.export(r, "assToVtt", () => n)
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "1VRQn": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");

        function i(e, t) {
            let r = document.createElement("a");
            r.style.display = "none", r.href = e, r.download = t, document.body.appendChild(r), r.click(), document.body.removeChild(r)
        }

        a.defineInteropFlag(r), a.export(r, "getExt", () => function e(t) {
            return t.includes("?") ? e(t.split("?")[0]) : t.includes("#") ? e(t.split("#")[0]) : t.trim().toLowerCase().split(".").pop()
        }), a.export(r, "download", () => i)
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "3weX2": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "def", () => i), a.export(r, "has", () => n), a.export(r, "get", () => s), a.export(r, "mergeDeep", () => function e(...t) {
            let r = e => e && "object" == typeof e && !Array.isArray(e);
            return t.reduce((t, a) => (Object.keys(a).forEach(i => {
                let o = t[i], n = a[i];
                Array.isArray(o) && Array.isArray(n) ? t[i] = o.concat(...n) : r(o) && r(n) ? t[i] = e(o, n) : t[i] = n
            }), t), {})
        });
        let i = Object.defineProperty, {hasOwnProperty: o} = Object.prototype;

        function n(e, t) {
            return o.call(e, t)
        }

        function s(e, t) {
            return Object.getOwnPropertyDescriptor(e, t)
        }
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "7kBIx": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");

        function i(e = 0) {
            return new Promise(t => setTimeout(t, e))
        }

        function o(e, t) {
            let r;
            return function (...a) {
                clearTimeout(r), r = setTimeout(() => (r = null, e.apply(this, a)), t)
            }
        }

        function n(e, t) {
            let r = !1;
            return function (...a) {
                r || (e.apply(this, a), r = !0, setTimeout(function () {
                    r = !1
                }, t))
            }
        }

        a.defineInteropFlag(r), a.export(r, "sleep", () => i), a.export(r, "debounce", () => o), a.export(r, "throttle", () => n)
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "13atT": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");

        function i(e, t, r) {
            return Math.max(Math.min(e, Math.max(t, r)), Math.min(t, r))
        }

        function o(e) {
            return e.charAt(0).toUpperCase() + e.slice(1)
        }

        function n(e) {
            return ["string", "number"].includes(typeof e)
        }

        function s(e) {
            if (!e) return "00:00";
            let t = Math.floor(e / 3600), r = Math.floor((e - 3600 * t) / 60), a = Math.floor(e - 3600 * t - 60 * r);
            return (t > 0 ? [t, r, a] : [r, a]).map(e => e < 10 ? `0${e}` : String(e)).join(":")
        }

        function l(e) {
            return e.replace(/[&<>'"]/g, e => ({
                "&": "&amp;",
                "<": "&lt;",
                ">": "&gt;",
                "'": "&#39;",
                '"': "&quot;"
            })[e] || e)
        }

        function c(e) {
            let t = {"&amp;": "&", "&lt;": "<", "&gt;": ">", "&#39;": "'", "&quot;": '"'},
                r = RegExp(`(${Object.keys(t).join("|")})`, "g");
            return e.replace(r, e => t[e] || e)
        }

        a.defineInteropFlag(r), a.export(r, "clamp", () => i), a.export(r, "capitalize", () => o), a.export(r, "isStringOrNumber", () => n), a.export(r, "secondToTime", () => s), a.export(r, "escape", () => l), a.export(r, "unescape", () => c)
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    AdvwB: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "ComponentOption", () => d);
        var i = e("../utils");
        let o = "array", n = "boolean", s = "string", l = "number", c = "object", u = "function";

        function p(e, t, r) {
            return (0, i.errorHandle)(t === s || t === l || e instanceof Element, `${r.join(".")} require '${s}' or 'Element' type`)
        }

        let d = {
            html: p,
            disable: `?${n}`,
            name: `?${s}`,
            index: `?${l}`,
            style: `?${c}`,
            click: `?${u}`,
            mounted: `?${u}`,
            tooltip: `?${s}|${l}`,
            width: `?${l}`,
            selector: `?${o}`,
            onSelect: `?${u}`,
            switch: `?${n}`,
            onSwitch: `?${u}`,
            range: `?${o}`,
            onRange: `?${u}`,
            onChange: `?${u}`
        };
        r.default = {
            id: s,
            container: p,
            url: s,
            poster: s,
            type: s,
            theme: s,
            lang: s,
            volume: l,
            isLive: n,
            muted: n,
            autoplay: n,
            autoSize: n,
            autoMini: n,
            loop: n,
            flip: n,
            playbackRate: n,
            aspectRatio: n,
            screenshot: n,
            setting: n,
            hotkey: n,
            pip: n,
            mutex: n,
            backdrop: n,
            fullscreen: n,
            fullscreenWeb: n,
            subtitleOffset: n,
            miniProgressBar: n,
            useSSR: n,
            playsInline: n,
            lock: n,
            fastForward: n,
            autoPlayback: n,
            autoOrientation: n,
            airplay: n,
            plugins: [u],
            layers: [d],
            contextmenu: [d],
            settings: [d],
            controls: [{
                ...d, position: (e, t, r) => {
                    let a = ["top", "left", "right"];
                    return (0, i.errorHandle)(a.includes(e), `${r.join(".")} only accept ${a.toString()} as parameters`)
                }
            }],
            quality: [{default: `?${n}`, html: s, url: s}],
            highlight: [{time: l, text: s}],
            thumbnails: {url: s, number: l, column: l, width: l, height: l},
            subtitle: {url: s, name: s, type: s, style: c, escape: n, encoding: s, onVttLoad: u},
            moreVideoAttr: c,
            i18n: c,
            icons: c,
            cssVar: c,
            customType: c
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "9Xmqu": [function (e, t, r) {
        e("@parcel/transformer-js/src/esmodule-helpers.js").defineInteropFlag(r), r.default = {
            propertys: ["audioTracks", "autoplay", "buffered", "controller", "controls", "crossOrigin", "currentSrc", "currentTime", "defaultMuted", "defaultPlaybackRate", "duration", "ended", "error", "loop", "mediaGroup", "muted", "networkState", "paused", "playbackRate", "played", "preload", "readyState", "seekable", "seeking", "src", "startDate", "textTracks", "videoTracks", "volume"],
            methods: ["addTextTrack", "canPlayType", "load", "play", "pause"],
            events: ["abort", "canplay", "canplaythrough", "durationchange", "emptied", "ended", "error", "loadeddata", "loadedmetadata", "loadstart", "pause", "play", "playing", "progress", "ratechange", "seeked", "seeking", "stalled", "suspend", "timeupdate", "volumechange", "waiting"],
            prototypes: ["width", "height", "videoWidth", "videoHeight", "poster", "webkitDecodedFrameCount", "webkitDroppedFrameCount", "playsInline", "webkitSupportsFullscreen", "webkitDisplayingFullscreen", "onenterpictureinpicture", "onleavepictureinpicture", "disablePictureInPicture", "cancelVideoFrameCallback", "requestVideoFrameCallback", "getVideoPlaybackQuality", "requestPictureInPicture", "webkitEnterFullScreen", "webkitEnterFullscreen", "webkitExitFullScreen", "webkitExitFullscreen"]
        }
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "2gKYH": [function (e, t, r) {
        e("@parcel/transformer-js/src/esmodule-helpers.js").defineInteropFlag(r);
        var a = e("./utils");

        class i {
            constructor(e) {
                this.art = e;
                let {option: t, constructor: r} = e;
                t.container instanceof Element ? this.$container = t.container : (this.$container = (0, a.query)(t.container), (0, a.errorHandle)(this.$container, `No container element found by ${t.container}`)), (0, a.errorHandle)((0, a.supportsFlex)(), "The current browser does not support flex layout");
                let i = this.$container.tagName.toLowerCase();
                (0, a.errorHandle)("div" === i, `Unsupported container element type, only support 'div' but got '${i}'`), (0, a.errorHandle)(r.instances.every(e => e.template.$container !== this.$container), "Cannot mount multiple instances on the same dom element"), this.query = this.query.bind(this), this.$container.dataset.artId = e.id, this.init()
            }

            static get html() {
                return `<div class="art-video-player art-subtitle-show art-layer-show art-control-show art-mask-show"><video class="art-video"><track default kind="metadata" src=""></track></video><div class="art-poster"></div><div class="art-subtitle"></div><div class="art-danmuku"></div><div class="art-layers"></div><div class="art-mask"><div class="art-state"></div></div><div class="art-bottom"><div class="art-progress"></div><div class="art-controls"><div class="art-controls-left"></div><div class="art-controls-center"></div><div class="art-controls-right"></div></div></div><div class="art-loading"></div><div class="art-notice"><div class="art-notice-inner"></div></div><div class="art-settings"></div><div class="art-info"><div class="art-info-panel"><div class="art-info-item"><div class="art-info-title">Player version:</div><div class="art-info-content">5.1.2</div></div><div class="art-info-item"><div class="art-info-title">Video url:</div><div class="art-info-content" data-video="src"></div></div><div class="art-info-item"><div class="art-info-title">Video volume:</div><div class="art-info-content" data-video="volume"></div></div><div class="art-info-item"><div class="art-info-title">Video time:</div><div class="art-info-content" data-video="currentTime"></div></div><div class="art-info-item"><div class="art-info-title">Video duration:</div><div class="art-info-content" data-video="duration"></div></div><div class="art-info-item"><div class="art-info-title">Video resolution:</div><div class="art-info-content"><span data-video="videoWidth"></span>x<span data-video="videoHeight"></span></div></div></div><div class="art-info-close">[x]</div></div><div class="art-contextmenus"></div></div>`
            }

            query(e) {
                return (0, a.query)(e, this.$container)
            }

            init() {
                let {option: e} = this.art;
                e.useSSR || (this.$container.innerHTML = i.html), this.$player = this.query(".art-video-player"), this.$video = this.query(".art-video"), this.$track = this.query("track"), this.$poster = this.query(".art-poster"), this.$subtitle = this.query(".art-subtitle"), this.$danmuku = this.query(".art-danmuku"), this.$bottom = this.query(".art-bottom"), this.$progress = this.query(".art-progress"), this.$controls = this.query(".art-controls"), this.$controlsLeft = this.query(".art-controls-left"), this.$controlsCenter = this.query(".art-controls-center"), this.$controlsRight = this.query(".art-controls-right"), this.$layer = this.query(".art-layers"), this.$loading = this.query(".art-loading"), this.$notice = this.query(".art-notice"), this.$noticeInner = this.query(".art-notice-inner"), this.$mask = this.query(".art-mask"), this.$state = this.query(".art-state"), this.$setting = this.query(".art-settings"), this.$info = this.query(".art-info"), this.$infoPanel = this.query(".art-info-panel"), this.$infoClose = this.query(".art-info-close"), this.$contextmenu = this.query(".art-contextmenus"), e.backdrop && (0, a.addClass)(this.$player, "art-backdrop"), a.isMobile && (0, a.addClass)(this.$player, "art-mobile")
            }

            destroy(e) {
                e ? this.$container.innerHTML = "" : (0, a.addClass)(this.$player, "art-destroy")
            }
        }

        r.default = i
    }, {"./utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "1AdeF": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("../utils"), o = e("./zh-cn"), n = a.interopDefault(o);
        r.default = class {
            constructor(e) {
                this.art = e, this.languages = {"zh-cn": n.default}, this.language = {}, this.update(e.option.i18n)
            }

            init() {
                let e = this.art.option.lang.toLowerCase();
                this.language = this.languages[e] || {}
            }

            get(e) {
                return this.language[e] || e
            }

            update(e) {
                this.languages = (0, i.mergeDeep)(this.languages, e), this.init()
            }
        }
    }, {"../utils": "h3rH9", "./zh-cn": "3ZSKq", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "3ZSKq": [function (e, t, r) {
        e("@parcel/transformer-js/src/esmodule-helpers.js").defineInteropFlag(r);
        let a = {
            "Video Info": "统计信息",
            Close: "关闭",
            "Video Load Failed": "加载失败",
            Volume: "音量",
            Play: "播放",
            Pause: "暂停",
            Rate: "速度",
            Mute: "静音",
            "Video Flip": "画面翻转",
            Horizontal: "水平",
            Vertical: "垂直",
            Reconnect: "重新连接",
            "Show Setting": "显示设置",
            "Hide Setting": "隐藏设置",
            Screenshot: "截图",
            "Play Speed": "播放速度",
            "Aspect Ratio": "画面比例",
            Default: "默认",
            Normal: "正常",
            Open: "打开",
            "Switch Video": "切换",
            "Switch Subtitle": "切换字幕",
            Fullscreen: "全屏",
            "Exit Fullscreen": "退出全屏",
            "Web Fullscreen": "网页全屏",
            "Exit Web Fullscreen": "退出网页全屏",
            "Mini Player": "迷你播放器",
            "PIP Mode": "开启画中画",
            "Exit PIP Mode": "退出画中画",
            "PIP Not Supported": "不支持画中画",
            "Fullscreen Not Supported": "不支持全屏",
            "Subtitle Offset": "字幕偏移",
            "Last Seen": "上次看到",
            "Jump Play": "跳转播放",
            AirPlay: "隔空播放",
            "AirPlay Not Available": "隔空播放不可用"
        };
        r.default = a, "undefined" != typeof window && (window["artplayer-i18n-zh-cn"] = a)
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "556MW": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("./urlMix"), o = a.interopDefault(i), n = e("./attrMix"), s = a.interopDefault(n), l = e("./playMix"),
            c = a.interopDefault(l), u = e("./pauseMix"), p = a.interopDefault(u), d = e("./toggleMix"),
            f = a.interopDefault(d), h = e("./seekMix"), m = a.interopDefault(h), g = e("./volumeMix"),
            v = a.interopDefault(g), y = e("./currentTimeMix"), b = a.interopDefault(y), x = e("./durationMix"),
            w = a.interopDefault(x), j = e("./switchMix"), k = a.interopDefault(j), S = e("./playbackRateMix"),
            I = a.interopDefault(S), T = e("./aspectRatioMix"), O = a.interopDefault(T), E = e("./screenshotMix"),
            M = a.interopDefault(E), $ = e("./fullscreenMix"), F = a.interopDefault($), C = e("./fullscreenWebMix"),
            H = a.interopDefault(C), D = e("./pipMix"), B = a.interopDefault(D), z = e("./loadedMix"),
            A = a.interopDefault(z), R = e("./playedMix"), L = a.interopDefault(R), P = e("./playingMix"),
            N = a.interopDefault(P), Z = e("./autoSizeMix"), _ = a.interopDefault(Z), q = e("./rectMix"),
            V = a.interopDefault(q), W = e("./flipMix"), U = a.interopDefault(W), Y = e("./miniMix"),
            K = a.interopDefault(Y), X = e("./posterMix"), G = a.interopDefault(X), J = e("./autoHeightMix"),
            Q = a.interopDefault(J), ee = e("./cssVarMix"), et = a.interopDefault(ee), er = e("./themeMix"),
            ea = a.interopDefault(er), ei = e("./typeMix"), eo = a.interopDefault(ei), en = e("./stateMix"),
            es = a.interopDefault(en), el = e("./subtitleOffsetMix"), ec = a.interopDefault(el), eu = e("./airplayMix"),
            ep = a.interopDefault(eu), ed = e("./qualityMix"), ef = a.interopDefault(ed), eh = e("./optionInit"),
            em = a.interopDefault(eh), eg = e("./eventInit"), ev = a.interopDefault(eg);
        r.default = class {
            constructor(e) {
                (0, o.default)(e), (0, s.default)(e), (0, c.default)(e), (0, p.default)(e), (0, f.default)(e), (0, m.default)(e), (0, v.default)(e), (0, b.default)(e), (0, w.default)(e), (0, k.default)(e), (0, I.default)(e), (0, O.default)(e), (0, M.default)(e), (0, F.default)(e), (0, H.default)(e), (0, B.default)(e), (0, A.default)(e), (0, L.default)(e), (0, N.default)(e), (0, _.default)(e), (0, V.default)(e), (0, U.default)(e), (0, K.default)(e), (0, G.default)(e), (0, Q.default)(e), (0, et.default)(e), (0, ea.default)(e), (0, eo.default)(e), (0, es.default)(e), (0, ec.default)(e), (0, ep.default)(e), (0, ef.default)(e), (0, ev.default)(e), (0, em.default)(e)
            }
        }
    }, {
        "./urlMix": "2mRAc",
        "./attrMix": "2EA19",
        "./playMix": "fD2Tc",
        "./pauseMix": "c3LGJ",
        "./toggleMix": "fVsAa",
        "./seekMix": "dmROF",
        "./volumeMix": "9jtfB",
        "./currentTimeMix": "7NCDR",
        "./durationMix": "YS7JL",
        "./switchMix": "dzUqN",
        "./playbackRateMix": "5I2mT",
        "./aspectRatioMix": "7m6R8",
        "./screenshotMix": "2dgtR",
        "./fullscreenMix": "fKDW8",
        "./fullscreenWebMix": "lNvYI",
        "./pipMix": "8j7oC",
        "./loadedMix": "dwVOT",
        "./playedMix": "dDeLx",
        "./playingMix": "ceoBp",
        "./autoSizeMix": "lcWXX",
        "./rectMix": "f7y88",
        "./flipMix": "l4qt5",
        "./miniMix": "9ZPBQ",
        "./posterMix": "5K8hA",
        "./autoHeightMix": "3T5ls",
        "./cssVarMix": "6KfHs",
        "./themeMix": "7lcSc",
        "./typeMix": "8JgTw",
        "./stateMix": "cebt1",
        "./subtitleOffsetMix": "hJvIy",
        "./airplayMix": "4Tp0U",
        "./qualityMix": "3wZgN",
        "./optionInit": "iPdgW",
        "./eventInit": "3mj0J",
        "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"
    }],
    "2mRAc": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {option: t, template: {$video: r}} = e;
            (0, i.def)(e, "url", {
                get: () => r.src, async set(a) {
                    if (a) {
                        let o = e.url, n = t.type || (0, i.getExt)(a), s = t.customType[n];
                        n && s ? (await (0, i.sleep)(), e.loading.show = !0, s.call(e, r, a, e)) : (URL.revokeObjectURL(o), r.src = a), o !== e.url && (e.option.url = a, e.isReady && o && e.once("video:canplay", () => {
                            e.emit("restart", a)
                        }))
                    } else await (0, i.sleep)(), e.loading.show = !0
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "2EA19": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {template: {$video: t}} = e;
            (0, i.def)(e, "attr", {
                value(e, r) {
                    if (void 0 === r) return t[e];
                    t[e] = r
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    fD2Tc: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {i18n: t, notice: r, option: a, constructor: {instances: o}, template: {$video: n}} = e;
            (0, i.def)(e, "play", {
                value: async function () {
                    let i = await n.play();
                    if (r.show = t.get("Play"), e.emit("play"), a.mutex) for (let t = 0; t < o.length; t++) {
                        let r = o[t];
                        r !== e && r.pause()
                    }
                    return i
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    c3LGJ: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {template: {$video: t}, i18n: r, notice: a} = e;
            (0, i.def)(e, "pause", {
                value() {
                    let i = t.pause();
                    return a.show = r.get("Pause"), e.emit("pause"), i
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    fVsAa: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            (0, i.def)(e, "toggle", {value: () => e.playing ? e.pause() : e.play()})
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    dmROF: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {notice: t} = e;
            (0, i.def)(e, "seek", {
                set(r) {
                    e.currentTime = r, e.emit("seek", e.currentTime), e.duration && (t.show = `${(0, i.secondToTime)(e.currentTime)} / ${(0, i.secondToTime)(e.duration)}`)
                }
            }), (0, i.def)(e, "forward", {
                set(t) {
                    e.seek = e.currentTime + t
                }
            }), (0, i.def)(e, "backward", {
                set(t) {
                    e.seek = e.currentTime - t
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "9jtfB": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {template: {$video: t}, i18n: r, notice: a, storage: o} = e;
            (0, i.def)(e, "volume", {
                get: () => t.volume || 0, set: e => {
                    t.volume = (0, i.clamp)(e, 0, 1), a.show = `${r.get("Volume")}: ${parseInt(100 * t.volume, 10)}`, 0 !== t.volume && o.set("volume", t.volume)
                }
            }), (0, i.def)(e, "muted", {
                get: () => t.muted, set: r => {
                    t.muted = r, e.emit("muted", r)
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "7NCDR": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {$video: t} = e.template;
            (0, i.def)(e, "currentTime", {
                get: () => t.currentTime || 0, set: r => {
                    Number.isNaN(r = parseFloat(r)) || (t.currentTime = (0, i.clamp)(r, 0, e.duration))
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    YS7JL: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            (0, i.def)(e, "duration", {
                get: () => {
                    let {duration: t} = e.template.$video;
                    return t === 1 / 0 ? 0 : t || 0
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    dzUqN: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            function t(t, r) {
                return new Promise((a, i) => {
                    if (t === e.url) return;
                    let {playing: o, aspectRatio: n, playbackRate: s} = e;
                    e.pause(), e.url = t, e.notice.show = "", e.once("video:error", i), e.once("video:loadedmetadata", () => {
                        e.currentTime = r
                    }), e.once("video:canplay", async () => {
                        e.playbackRate = s, e.aspectRatio = n, o && await e.play(), e.notice.show = "", a()
                    })
                })
            }

            (0, i.def)(e, "switchQuality", {value: r => t(r, e.currentTime)}), (0, i.def)(e, "switchUrl", {value: e => t(e, 0)}), (0, i.def)(e, "switch", {set: e.switchUrl})
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "5I2mT": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {template: {$video: t}, i18n: r, notice: a} = e;
            (0, i.def)(e, "playbackRate", {
                get: () => t.playbackRate, set(i) {
                    i ? i !== t.playbackRate && (t.playbackRate = i, a.show = `${r.get("Rate")}: ${1 === i ? r.get("Normal") : `${i}x`}`) : e.playbackRate = 1
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "7m6R8": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {i18n: t, notice: r, template: {$video: a, $player: o}} = e;
            (0, i.def)(e, "aspectRatio", {
                get: () => o.dataset.aspectRatio || "default", set(n) {
                    if (n || (n = "default"), "default" === n) (0, i.setStyle)(a, "width", null), (0, i.setStyle)(a, "height", null), (0, i.setStyle)(a, "margin", null), delete o.dataset.aspectRatio; else {
                        let e = n.split(":").map(Number), {clientWidth: t, clientHeight: r} = o, s = e[0] / e[1];
                        t / r > s ? ((0, i.setStyle)(a, "width", `${s * r}px`), (0, i.setStyle)(a, "height", "100%"), (0, i.setStyle)(a, "margin", "0 auto")) : ((0, i.setStyle)(a, "width", "100%"), (0, i.setStyle)(a, "height", `${t / s}px`), (0, i.setStyle)(a, "margin", "auto 0")), o.dataset.aspectRatio = n
                    }
                    r.show = `${t.get("Aspect Ratio")}: ${"default" === n ? t.get("Default") : n}`, e.emit("aspectRatio", n)
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "2dgtR": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {notice: t, template: {$video: r}} = e, a = (0, i.createElement)("canvas");
            (0, i.def)(e, "getDataURL", {
                value: () => new Promise((e, i) => {
                    try {
                        a.width = r.videoWidth, a.height = r.videoHeight, a.getContext("2d").drawImage(r, 0, 0), e(a.toDataURL("image/png"))
                    } catch (e) {
                        t.show = e, i(e)
                    }
                })
            }), (0, i.def)(e, "getBlobUrl", {
                value: () => new Promise((e, i) => {
                    try {
                        a.width = r.videoWidth, a.height = r.videoHeight, a.getContext("2d").drawImage(r, 0, 0), a.toBlob(t => {
                            e(URL.createObjectURL(t))
                        })
                    } catch (e) {
                        t.show = e, i(e)
                    }
                })
            }), (0, i.def)(e, "screenshot", {
                value: async () => {
                    let t = await e.getDataURL();
                    return (0, i.download)(t, `artplayer_${(0, i.secondToTime)(r.currentTime)}.png`), e.emit("screenshot", t), t
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    fKDW8: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => s);
        var i = e("../libs/screenfull"), o = a.interopDefault(i), n = e("../utils");

        function s(e) {
            let {i18n: t, notice: r, template: {$video: a, $player: i}} = e, s = e => {
                (0, o.default).on("change", () => {
                    e.emit("fullscreen", o.default.isFullscreen)
                }), (0, o.default).on("error", t => {
                    e.emit("fullscreenError", t)
                }), (0, n.def)(e, "fullscreen", {
                    get: () => o.default.isFullscreen, async set(t) {
                        t ? (e.state = "fullscreen", await (0, o.default).request(i), (0, n.addClass)(i, "art-fullscreen")) : (await (0, o.default).exit(), (0, n.removeClass)(i, "art-fullscreen")), e.emit("resize")
                    }
                })
            }, l = e => {
                (0, n.def)(e, "fullscreen", {
                    get: () => a.webkitDisplayingFullscreen, set(t) {
                        t ? (e.state = "fullscreen", a.webkitEnterFullscreen(), e.emit("fullscreen", !0)) : (a.webkitExitFullscreen(), e.emit("fullscreen", !1)), e.emit("resize")
                    }
                })
            };
            e.once("video:loadedmetadata", () => {
                o.default.isEnabled ? s(e) : document.fullscreenEnabled || a.webkitSupportsFullscreen ? l(e) : (0, n.def)(e, "fullscreen", {
                    get: () => !1,
                    set() {
                        r.show = t.get("Fullscreen Not Supported")
                    }
                }), (0, n.def)(e, "fullscreen", (0, n.get)(e, "fullscreen"))
            })
        }
    }, {"../libs/screenfull": "lUahW", "../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    lUahW: [function (e, t, r) {
        e("@parcel/transformer-js/src/esmodule-helpers.js").defineInteropFlag(r);
        let a = [["requestFullscreen", "exitFullscreen", "fullscreenElement", "fullscreenEnabled", "fullscreenchange", "fullscreenerror"], ["webkitRequestFullscreen", "webkitExitFullscreen", "webkitFullscreenElement", "webkitFullscreenEnabled", "webkitfullscreenchange", "webkitfullscreenerror"], ["webkitRequestFullScreen", "webkitCancelFullScreen", "webkitCurrentFullScreenElement", "webkitCancelFullScreen", "webkitfullscreenchange", "webkitfullscreenerror"], ["mozRequestFullScreen", "mozCancelFullScreen", "mozFullScreenElement", "mozFullScreenEnabled", "mozfullscreenchange", "mozfullscreenerror"], ["msRequestFullscreen", "msExitFullscreen", "msFullscreenElement", "msFullscreenEnabled", "MSFullscreenChange", "MSFullscreenError"]],
            i = (() => {
                if ("undefined" == typeof document) return !1;
                let e = a[0], t = {};
                for (let r of a) if (r[1] in document) {
                    for (let [a, i] of r.entries()) t[e[a]] = i;
                    return t
                }
                return !1
            })(), o = {change: i.fullscreenchange, error: i.fullscreenerror}, n = {
                request: (e = document.documentElement, t) => new Promise((r, a) => {
                    let o = () => {
                        n.off("change", o), r()
                    };
                    n.on("change", o);
                    let s = e[i.requestFullscreen](t);
                    s instanceof Promise && s.then(o).catch(a)
                }), exit: () => new Promise((e, t) => {
                    if (!n.isFullscreen) {
                        e();
                        return
                    }
                    let r = () => {
                        n.off("change", r), e()
                    };
                    n.on("change", r);
                    let a = document[i.exitFullscreen]();
                    a instanceof Promise && a.then(r).catch(t)
                }), toggle: (e, t) => n.isFullscreen ? n.exit() : n.request(e, t), onchange(e) {
                    n.on("change", e)
                }, onerror(e) {
                    n.on("error", e)
                }, on(e, t) {
                    let r = o[e];
                    r && document.addEventListener(r, t, !1)
                }, off(e, t) {
                    let r = o[e];
                    r && document.removeEventListener(r, t, !1)
                }, raw: i
            };
        Object.defineProperties(n, {
            isFullscreen: {get: () => !!document[i.fullscreenElement]},
            element: {enumerable: !0, get: () => document[i.fullscreenElement]},
            isEnabled: {enumerable: !0, get: () => !!document[i.fullscreenEnabled]}
        }), i || (n = {isEnabled: !1}), r.default = n
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    lNvYI: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {constructor: t, template: {$container: r, $player: a}} = e, o = "";
            (0, i.def)(e, "fullscreenWeb", {
                get: () => (0, i.hasClass)(a, "art-fullscreen-web"), set(n) {
                    n ? (o = a.style.cssText, t.FULLSCREEN_WEB_IN_BODY && (0, i.append)(document.body, a), e.state = "fullscreenWeb", (0, i.setStyle)(a, "width", "100%"), (0, i.setStyle)(a, "height", "100%"), (0, i.addClass)(a, "art-fullscreen-web"), e.emit("fullscreenWeb", !0)) : (t.FULLSCREEN_WEB_IN_BODY && (0, i.append)(r, a), o && (a.style.cssText = o, o = ""), (0, i.removeClass)(a, "art-fullscreen-web"), e.emit("fullscreenWeb", !1)), e.emit("resize")
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "8j7oC": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {i18n: t, notice: r, template: {$video: a}} = e;
            document.pictureInPictureEnabled ? function (e) {
                let {template: {$video: t}, proxy: r, notice: a} = e;
                t.disablePictureInPicture = !1, (0, i.def)(e, "pip", {
                    get: () => document.pictureInPictureElement,
                    set(r) {
                        r ? (e.state = "pip", t.requestPictureInPicture().catch(e => {
                            throw a.show = e, e
                        })) : document.exitPictureInPicture().catch(e => {
                            throw a.show = e, e
                        })
                    }
                }), r(t, "enterpictureinpicture", () => {
                    e.emit("pip", !0)
                }), r(t, "leavepictureinpicture", () => {
                    e.emit("pip", !1)
                })
            }(e) : a.webkitSupportsPresentationMode ? function (e) {
                let {$video: t} = e.template;
                t.webkitSetPresentationMode("inline"), (0, i.def)(e, "pip", {
                    get: () => "picture-in-picture" === t.webkitPresentationMode,
                    set(r) {
                        r ? (e.state = "pip", t.webkitSetPresentationMode("picture-in-picture"), e.emit("pip", !0)) : (t.webkitSetPresentationMode("inline"), e.emit("pip", !1))
                    }
                })
            }(e) : (0, i.def)(e, "pip", {
                get: () => !1, set() {
                    r.show = t.get("PIP Not Supported")
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    dwVOT: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {$video: t} = e.template;
            (0, i.def)(e, "loaded", {get: () => e.loadedTime / t.duration}), (0, i.def)(e, "loadedTime", {get: () => t.buffered.length ? t.buffered.end(t.buffered.length - 1) : 0})
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    dDeLx: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            (0, i.def)(e, "played", {get: () => e.currentTime / e.duration})
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    ceoBp: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {$video: t} = e.template;
            (0, i.def)(e, "playing", {get: () => !!(t.currentTime > 0 && !t.paused && !t.ended && t.readyState > 2)})
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    lcWXX: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {$container: t, $player: r, $video: a} = e.template;
            (0, i.def)(e, "autoSize", {
                value() {
                    let {videoWidth: o, videoHeight: n} = a, {width: s, height: l} = t.getBoundingClientRect(),
                        c = o / n;
                    s / l > c ? ((0, i.setStyle)(r, "width", `${l * c / s * 100}%`), (0, i.setStyle)(r, "height", "100%")) : ((0, i.setStyle)(r, "width", "100%"), (0, i.setStyle)(r, "height", `${s / c / l * 100}%`)), e.emit("autoSize", {
                        width: e.width,
                        height: e.height
                    })
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    f7y88: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            (0, i.def)(e, "rect", {get: () => e.template.$player.getBoundingClientRect()});
            let t = ["bottom", "height", "left", "right", "top", "width"];
            for (let r = 0; r < t.length; r++) {
                let a = t[r];
                (0, i.def)(e, a, {get: () => e.rect[a]})
            }
            (0, i.def)(e, "x", {get: () => e.left + window.pageXOffset}), (0, i.def)(e, "y", {get: () => e.top + window.pageYOffset})
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    l4qt5: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {template: {$player: t}, i18n: r, notice: a} = e;
            (0, i.def)(e, "flip", {
                get: () => t.dataset.flip || "normal", set(o) {
                    o || (o = "normal"), "normal" === o ? delete t.dataset.flip : t.dataset.flip = o, a.show = `${r.get("Video Flip")}: ${r.get((0, i.capitalize)(o))}`, e.emit("flip", o)
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "9ZPBQ": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {icons: t, proxy: r, storage: a, template: {$player: o, $video: n}} = e, s = !1, l = 0, c = 0;

            function u() {
                let {$mini: t} = e.template;
                t && ((0, i.removeClass)(o, "art-mini"), (0, i.setStyle)(t, "display", "none"), o.prepend(n), e.emit("mini", !1))
            }

            function p(t, r) {
                e.playing ? ((0, i.setStyle)(t, "display", "none"), (0, i.setStyle)(r, "display", "flex")) : ((0, i.setStyle)(t, "display", "flex"), (0, i.setStyle)(r, "display", "none"))
            }

            function d() {
                let {$mini: t} = e.template, r = t.getBoundingClientRect(), o = window.innerHeight - r.height - 50,
                    n = window.innerWidth - r.width - 50;
                a.set("top", o), a.set("left", n), (0, i.setStyle)(t, "top", `${o}px`), (0, i.setStyle)(t, "left", `${n}px`)
            }

            (0, i.def)(e, "mini", {
                get: () => (0, i.hasClass)(o, "art-mini"), set(f) {
                    if (f) {
                        e.state = "mini", (0, i.addClass)(o, "art-mini");
                        let f = function () {
                            let {$mini: o} = e.template;
                            if (o) return (0, i.append)(o, n), (0, i.setStyle)(o, "display", "flex");
                            {
                                let o = (0, i.createElement)("div");
                                (0, i.addClass)(o, "art-mini-popup"), (0, i.append)(document.body, o), e.template.$mini = o, (0, i.append)(o, n);
                                let d = (0, i.append)(o, '<div class="art-mini-close"></div>');
                                (0, i.append)(d, t.close), r(d, "click", u);
                                let f = (0, i.append)(o, '<div class="art-mini-state"></div>'),
                                    h = (0, i.append)(f, t.play), m = (0, i.append)(f, t.pause);
                                return r(h, "click", () => e.play()), r(m, "click", () => e.pause()), p(h, m), e.on("video:playing", () => p(h, m)), e.on("video:pause", () => p(h, m)), e.on("video:timeupdate", () => p(h, m)), r(o, "mousedown", e => {
                                    s = 0 === e.button, l = e.pageX, c = e.pageY
                                }), e.on("document:mousemove", e => {
                                    if (s) {
                                        (0, i.addClass)(o, "art-mini-droging");
                                        let t = e.pageX - l, r = e.pageY - c;
                                        (0, i.setStyle)(o, "transform", `translate(${t}px, ${r}px)`)
                                    }
                                }), e.on("document:mouseup", () => {
                                    if (s) {
                                        s = !1, (0, i.removeClass)(o, "art-mini-droging");
                                        let e = o.getBoundingClientRect();
                                        a.set("left", e.left), a.set("top", e.top), (0, i.setStyle)(o, "left", `${e.left}px`), (0, i.setStyle)(o, "top", `${e.top}px`), (0, i.setStyle)(o, "transform", null)
                                    }
                                }), o
                            }
                        }(), h = a.get("top"), m = a.get("left");
                        h && m ? ((0, i.setStyle)(f, "top", `${h}px`), (0, i.setStyle)(f, "left", `${m}px`), (0, i.isInViewport)(f) || d()) : d(), e.emit("mini", !0)
                    } else u()
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "5K8hA": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {template: {$poster: t}} = e;
            (0, i.def)(e, "poster", {
                get: () => {
                    try {
                        return t.style.backgroundImage.match(/"(.*)"/)[1]
                    } catch (e) {
                        return ""
                    }
                }, set(e) {
                    (0, i.setStyle)(t, "backgroundImage", `url(${e})`)
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "3T5ls": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {template: {$container: t, $video: r}} = e;
            (0, i.def)(e, "autoHeight", {
                value() {
                    let {clientWidth: a} = t, {videoHeight: o, videoWidth: n} = r, s = a / n * o;
                    (0, i.setStyle)(t, "height", s + "px"), e.emit("autoHeight", s)
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "6KfHs": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {$player: t} = e.template;
            (0, i.def)(e, "cssVar", {value: (e, r) => r ? t.style.setProperty(e, r) : getComputedStyle(t).getPropertyValue(e)})
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "7lcSc": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            (0, i.def)(e, "theme", {
                get: () => e.cssVar("--art-theme"), set(t) {
                    e.cssVar("--art-theme", t)
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "8JgTw": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            (0, i.def)(e, "type", {
                get: () => e.option.type, set(t) {
                    e.option.type = t
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    cebt1: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let t = ["mini", "pip", "fullscreen", "fullscreenWeb"];
            (0, i.def)(e, "state", {
                get: () => t.find(t => e[t]) || "standard", set(r) {
                    for (let a = 0; a < t.length; a++) {
                        let i = t[a];
                        i !== r && e[i] && (e[i] = !1)
                    }
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    hJvIy: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {clamp: t} = e.constructor.utils, {notice: r, template: a, i18n: o} = e, n = 0, s = [];
            e.on("subtitle:switch", () => {
                s = []
            }), (0, i.def)(e, "subtitleOffset", {
                get: () => n, set(i) {
                    if (a.$track && a.$track.track) {
                        let l = Array.from(a.$track.track.cues);
                        n = t(i, -5, 5);
                        for (let r = 0; r < l.length; r++) {
                            let a = l[r];
                            s[r] || (s[r] = {
                                startTime: a.startTime,
                                endTime: a.endTime
                            }), a.startTime = t(s[r].startTime + n, 0, e.duration), a.endTime = t(s[r].endTime + n, 0, e.duration)
                        }
                        e.subtitle.update(), r.show = `${o.get("Subtitle Offset")}: ${i}s`, e.emit("subtitleOffset", i)
                    } else e.emit("subtitleOffset", 0)
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "4Tp0U": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {i18n: t, notice: r, proxy: a, template: {$video: o}} = e, n = !0;
            window.WebKitPlaybackTargetAvailabilityEvent && o.webkitShowPlaybackTargetPicker ? a(o, "webkitplaybacktargetavailabilitychanged", e => {
                switch (e.availability) {
                    case"available":
                        n = !0;
                        break;
                    case"not-available":
                        n = !1
                }
            }) : n = !1, (0, i.def)(e, "airplay", {
                value() {
                    n ? (o.webkitShowPlaybackTargetPicker(), e.emit("airplay")) : r.show = t.get("AirPlay Not Available")
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "3wZgN": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            (0, i.def)(e, "quality", {
                set(t) {
                    let {controls: r, notice: a, i18n: i} = e, o = t.find(e => e.default) || t[0];
                    r.update({
                        name: "quality",
                        position: "right",
                        index: 10,
                        style: {marginRight: "10px"},
                        html: o ? o.html : "",
                        selector: t,
                        async onSelect(t) {
                            await e.switchQuality(t.url), a.show = `${i.get("Switch Video")}: ${t.html}`
                        }
                    })
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    iPdgW: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {option: t, storage: r, template: {$video: a, $poster: o}} = e;
            for (let r in t.moreVideoAttr) e.attr(r, t.moreVideoAttr[r]);
            t.muted && (e.muted = t.muted), t.volume && (a.volume = (0, i.clamp)(t.volume, 0, 1));
            let n = r.get("volume");
            for (let r in "number" == typeof n && (a.volume = (0, i.clamp)(n, 0, 1)), t.poster && (0, i.setStyle)(o, "backgroundImage", `url(${t.poster})`), t.autoplay && (a.autoplay = t.autoplay), t.playsInline && (a.playsInline = !0, a["webkit-playsinline"] = !0), t.theme && (t.cssVar["--art-theme"] = t.theme), t.cssVar) e.cssVar(r, t.cssVar[r]);
            e.url = t.url
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "3mj0J": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => s);
        var i = e("../config"), o = a.interopDefault(i), n = e("../utils");

        function s(e) {
            let {
                i18n: t,
                notice: r,
                option: a,
                constructor: i,
                proxy: s,
                template: {$player: l, $video: c, $poster: u}
            } = e, p = 0;
            for (let t = 0; t < o.default.events.length; t++) s(c, o.default.events[t], t => {
                e.emit(`video:${t.type}`, t)
            });
            e.on("video:canplay", () => {
                p = 0, e.loading.show = !1
            }), e.once("video:canplay", () => {
                e.loading.show = !1, e.controls.show = !0, e.mask.show = !0, e.isReady = !0, e.emit("ready")
            }), e.on("video:ended", () => {
                a.loop ? (e.seek = 0, e.play(), e.controls.show = !1, e.mask.show = !1) : (e.controls.show = !0, e.mask.show = !0)
            }), e.on("video:error", async o => {
                p < i.RECONNECT_TIME_MAX ? (await (0, n.sleep)(i.RECONNECT_SLEEP_TIME), p += 1, e.url = a.url, r.show = `${t.get("Reconnect")}: ${p}`, e.emit("error", o, p)) : (e.mask.show = !0, e.loading.show = !1, e.controls.show = !0, (0, n.addClass)(l, "art-error"), await (0, n.sleep)(i.RECONNECT_SLEEP_TIME), r.show = t.get("Video Load Failed"))
            }), e.on("video:loadedmetadata", () => {
                e.emit("resize"), n.isMobile && (e.loading.show = !1, e.controls.show = !0, e.mask.show = !0)
            }), e.on("video:loadstart", () => {
                e.loading.show = !0, e.mask.show = !1, e.controls.show = !0
            }), e.on("video:pause", () => {
                e.controls.show = !0, e.mask.show = !0
            }), e.on("video:play", () => {
                e.mask.show = !1, (0, n.setStyle)(u, "display", "none")
            }), e.on("video:playing", () => {
                e.mask.show = !1
            }), e.on("video:progress", () => {
                e.playing && (e.loading.show = !1)
            }), e.on("video:seeked", () => {
                e.loading.show = !1, e.mask.show = !0
            }), e.on("video:seeking", () => {
                e.loading.show = !0, e.mask.show = !1
            }), e.on("video:timeupdate", () => {
                e.mask.show = !1
            }), e.on("video:waiting", () => {
                e.loading.show = !0, e.mask.show = !1
            })
        }
    }, {"../config": "9Xmqu", "../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "14IBq": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("../utils"), o = e("../utils/component"), n = a.interopDefault(o), s = e("./fullscreen"),
            l = a.interopDefault(s), c = e("./fullscreenWeb"), u = a.interopDefault(c), p = e("./pip"),
            d = a.interopDefault(p), f = e("./playAndPause"), h = a.interopDefault(f), m = e("./progress"),
            g = a.interopDefault(m), v = e("./time"), y = a.interopDefault(v), b = e("./volume"),
            x = a.interopDefault(b), w = e("./setting"), j = a.interopDefault(w), k = e("./thumbnails"),
            S = a.interopDefault(k), I = e("./screenshot"), T = a.interopDefault(I), O = e("./airplay"),
            E = a.interopDefault(O);

        class M extends n.default {
            constructor(e) {
                super(e), this.isHover = !1, this.name = "control", this.timer = Date.now();
                let {constructor: t} = e, {$player: r, $bottom: a} = this.art.template;
                e.on("mousemove", () => {
                    i.isMobile || (this.show = !0)
                }), e.on("click", () => {
                    i.isMobile ? this.toggle() : this.show = !0
                }), e.on("document:mousemove", e => {
                    this.isHover = (0, i.includeFromEvent)(e, a)
                }), e.on("video:timeupdate", () => {
                    !e.setting.show && !this.isHover && !e.isInput && e.playing && this.show && Date.now() - this.timer >= t.CONTROL_HIDE_TIME && (this.show = !1)
                }), e.on("control", e => {
                    e ? ((0, i.removeClass)(r, "art-hide-cursor"), (0, i.addClass)(r, "art-hover"), this.timer = Date.now()) : ((0, i.addClass)(r, "art-hide-cursor"), (0, i.removeClass)(r, "art-hover"))
                }), this.init()
            }

            init() {
                let {option: e} = this.art;
                e.isLive || this.add((0, g.default)({
                    name: "progress",
                    position: "top",
                    index: 10
                })), !e.thumbnails.url || e.isLive || i.isMobile || this.add((0, S.default)({
                    name: "thumbnails",
                    position: "top",
                    index: 20
                })), this.add((0, h.default)({
                    name: "playAndPause",
                    position: "left",
                    index: 10
                })), this.add((0, x.default)({
                    name: "volume",
                    position: "left",
                    index: 20
                })), e.isLive || this.add((0, y.default)({
                    name: "time",
                    position: "left",
                    index: 30
                })), e.quality.length && (0, i.sleep)().then(() => {
                    this.art.quality = e.quality
                }), e.screenshot && !i.isMobile && this.add((0, T.default)({
                    name: "screenshot",
                    position: "right",
                    index: 20
                })), e.setting && this.add((0, j.default)({
                    name: "setting",
                    position: "right",
                    index: 30
                })), e.pip && this.add((0, d.default)({
                    name: "pip",
                    position: "right",
                    index: 40
                })), e.airplay && window.WebKitPlaybackTargetAvailabilityEvent && this.add((0, E.default)({
                    name: "airplay",
                    position: "right",
                    index: 50
                })), e.fullscreenWeb && this.add((0, u.default)({
                    name: "fullscreenWeb",
                    position: "right",
                    index: 60
                })), e.fullscreen && this.add((0, l.default)({name: "fullscreen", position: "right", index: 70}));
                for (let t = 0; t < e.controls.length; t++) this.add(e.controls[t])
            }

            add(e) {
                let t = "function" == typeof e ? e(this.art) : e, {
                    $progress: r,
                    $controlsLeft: a,
                    $controlsRight: o
                } = this.art.template;
                switch (t.position) {
                    case"top":
                        this.$parent = r;
                        break;
                    case"left":
                        this.$parent = a;
                        break;
                    case"right":
                        this.$parent = o;
                        break;
                    default:
                        (0, i.errorHandle)(!1, "Control option.position must one of 'top', 'left', 'right'")
                }
                super.add(t)
            }
        }

        r.default = M
    }, {
        "../utils": "h3rH9",
        "../utils/component": "guki8",
        "./fullscreen": "cxHNK",
        "./fullscreenWeb": "66eEC",
        "./pip": "kCFkA",
        "./playAndPause": "iRhgD",
        "./progress": "aBBSH",
        "./time": "7H0CE",
        "./volume": "lMwFm",
        "./setting": "8BrCu",
        "./thumbnails": "2HiWx",
        "./screenshot": "c1GeG",
        "./airplay": "6GRju",
        "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"
    }],
    guki8: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("./dom"), o = e("./format"), n = e("./error"), s = e("option-validator"), l = a.interopDefault(s),
            c = e("../scheme");
        r.default = class {
            constructor(e) {
                this.id = 0, this.art = e, this.cache = new Map, this.add = this.add.bind(this), this.remove = this.remove.bind(this), this.update = this.update.bind(this)
            }

            get show() {
                return (0, i.hasClass)(this.art.template.$player, `art-${this.name}-show`)
            }

            set show(e) {
                let {$player: t} = this.art.template, r = `art-${this.name}-show`;
                e ? (0, i.addClass)(t, r) : (0, i.removeClass)(t, r), this.art.emit(this.name, e)
            }

            toggle() {
                this.show = !this.show
            }

            add(e) {
                let t = "function" == typeof e ? e(this.art) : e;
                if (t.html = t.html || "", (0, l.default)(t, c.ComponentOption), !this.$parent || !this.name || t.disable) return;
                let r = t.name || `${this.name}${this.id}`, a = this.cache.get(r);
                (0, n.errorHandle)(!a, `Can't add an existing [${r}] to the [${this.name}]`), this.id += 1;
                let o = (0, i.createElement)("div");
                (0, i.addClass)(o, `art-${this.name}`), (0, i.addClass)(o, `art-${this.name}-${r}`);
                let s = Array.from(this.$parent.children);
                o.dataset.index = t.index || this.id;
                let u = s.find(e => Number(e.dataset.index) >= Number(o.dataset.index));
                u ? u.insertAdjacentElement("beforebegin", o) : (0, i.append)(this.$parent, o), t.html && (0, i.append)(o, t.html), t.style && (0, i.setStyles)(o, t.style), t.tooltip && (0, i.tooltip)(o, t.tooltip);
                let p = [];
                if (t.click) {
                    let e = this.art.events.proxy(o, "click", e => {
                        e.preventDefault(), t.click.call(this.art, this, e)
                    });
                    p.push(e)
                }
                return t.selector && ["left", "right"].includes(t.position) && this.addSelector(t, o, p), this[r] = o, this.cache.set(r, {
                    $ref: o,
                    events: p,
                    option: t
                }), t.mounted && t.mounted.call(this.art, o), o
            }

            addSelector(e, t, r) {
                let {hover: a, proxy: n} = this.art.events;
                (0, i.addClass)(t, "art-control-selector");
                let s = (0, i.createElement)("div");
                (0, i.addClass)(s, "art-selector-value"), (0, i.append)(s, e.html), t.innerText = "", (0, i.append)(t, s);
                let l = e.selector.map((e, t) => `<div class="art-selector-item ${e.default ? "art-current" : ""}" data-index="${t}">${e.html}</div>`).join(""),
                    c = (0, i.createElement)("div");
                (0, i.addClass)(c, "art-selector-list"), (0, i.append)(c, l), (0, i.append)(t, c);
                let u = () => {
                    let e = (0, i.getStyle)(t, "width"), r = (0, i.getStyle)(c, "width");
                    c.style.left = `${e / 2 - r / 2}px`
                };
                a(t, u);
                let p = n(c, "click", async t => {
                    let r = (t.composedPath() || []).find(e => (0, i.hasClass)(e, "art-selector-item"));
                    if (!r) return;
                    (0, i.inverseClass)(r, "art-current");
                    let a = Number(r.dataset.index), n = e.selector[a] || {};
                    if (s.innerText = r.innerText, e.onSelect) {
                        let a = await e.onSelect.call(this.art, n, r, t);
                        (0, o.isStringOrNumber)(a) && (s.innerHTML = a)
                    }
                    u()
                });
                r.push(p)
            }

            remove(e) {
                let t = this.cache.get(e);
                (0, n.errorHandle)(t, `Can't find [${e}] from the [${this.name}]`), t.option.beforeUnmount && t.option.beforeUnmount.call(this.art, t.$ref);
                for (let e = 0; e < t.events.length; e++) this.art.events.remove(t.events[e]);
                this.cache.delete(e), delete this[e], (0, i.remove)(t.$ref)
            }

            update(e) {
                let t = this.cache.get(e.name);
                return t && (e = Object.assign(t.option, e), this.remove(e.name)), this.add(e)
            }
        }
    }, {
        "./dom": "XgAQE",
        "./format": "13atT",
        "./error": "2nFlF",
        "option-validator": "9I0i9",
        "../scheme": "AdvwB",
        "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"
    }],
    cxHNK: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            return t => ({
                ...e, tooltip: t.i18n.get("Fullscreen"), mounted: e => {
                    let {proxy: r, icons: a, i18n: o} = t, n = (0, i.append)(e, a.fullscreenOn),
                        s = (0, i.append)(e, a.fullscreenOff);
                    (0, i.setStyle)(s, "display", "none"), r(e, "click", () => {
                        t.fullscreen = !t.fullscreen
                    }), t.on("fullscreen", t => {
                        t ? ((0, i.tooltip)(e, o.get("Exit Fullscreen")), (0, i.setStyle)(n, "display", "none"), (0, i.setStyle)(s, "display", "inline-flex")) : ((0, i.tooltip)(e, o.get("Fullscreen")), (0, i.setStyle)(n, "display", "inline-flex"), (0, i.setStyle)(s, "display", "none"))
                    })
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "66eEC": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            return t => ({
                ...e, tooltip: t.i18n.get("Web Fullscreen"), mounted: e => {
                    let {proxy: r, icons: a, i18n: o} = t, n = (0, i.append)(e, a.fullscreenWebOn),
                        s = (0, i.append)(e, a.fullscreenWebOff);
                    (0, i.setStyle)(s, "display", "none"), r(e, "click", () => {
                        t.fullscreenWeb = !t.fullscreenWeb
                    }), t.on("fullscreenWeb", t => {
                        t ? ((0, i.tooltip)(e, o.get("Exit Web Fullscreen")), (0, i.setStyle)(n, "display", "none"), (0, i.setStyle)(s, "display", "inline-flex")) : ((0, i.tooltip)(e, o.get("Web Fullscreen")), (0, i.setStyle)(n, "display", "inline-flex"), (0, i.setStyle)(s, "display", "none"))
                    })
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    kCFkA: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            return t => ({
                ...e, tooltip: t.i18n.get("PIP Mode"), mounted: e => {
                    let {proxy: r, icons: a, i18n: o} = t;
                    (0, i.append)(e, a.pip), r(e, "click", () => {
                        t.pip = !t.pip
                    }), t.on("pip", t => {
                        (0, i.tooltip)(e, o.get(t ? "Exit PIP Mode" : "PIP Mode"))
                    })
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    iRhgD: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            return t => ({
                ...e, mounted: e => {
                    let {proxy: r, icons: a, i18n: o} = t, n = (0, i.append)(e, a.play), s = (0, i.append)(e, a.pause);

                    function l() {
                        (0, i.setStyle)(n, "display", "flex"), (0, i.setStyle)(s, "display", "none")
                    }

                    function c() {
                        (0, i.setStyle)(n, "display", "none"), (0, i.setStyle)(s, "display", "flex")
                    }

                    (0, i.tooltip)(n, o.get("Play")), (0, i.tooltip)(s, o.get("Pause")), r(n, "click", () => {
                        t.play()
                    }), r(s, "click", () => {
                        t.pause()
                    }), t.playing ? c() : l(), t.on("video:playing", () => {
                        c()
                    }), t.on("video:pause", () => {
                        l()
                    })
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    aBBSH: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "getPosFromEvent", () => o), a.export(r, "setCurrentTime", () => n), a.export(r, "default", () => s);
        var i = e("../utils");

        function o(e, t) {
            let {$progress: r} = e.template, {left: a} = r.getBoundingClientRect(),
                o = i.isMobile ? t.touches[0].clientX : t.clientX, n = (0, i.clamp)(o - a, 0, r.clientWidth),
                s = n / r.clientWidth * e.duration, l = (0, i.secondToTime)(s),
                c = (0, i.clamp)(n / r.clientWidth, 0, 1);
            return {second: s, time: l, width: n, percentage: c}
        }

        function n(e, t) {
            if (e.isRotate) {
                let r = t.touches[0].clientY / e.height, a = r * e.duration;
                e.emit("setBar", "played", r), e.seek = a
            } else {
                let {second: r, percentage: a} = o(e, t);
                e.emit("setBar", "played", a), e.seek = r
            }
        }

        function s(e) {
            return t => {
                let {icons: r, option: a, proxy: s} = t;
                return {
                    ...e,
                    html: `<div class="art-control-progress-inner"><div class="art-progress-hover"></div><div class="art-progress-loaded"></div><div class="art-progress-played"></div><div class="art-progress-highlight"></div><div class="art-progress-indicator"></div><div class="art-progress-tip"></div></div>`,
                    mounted: e => {
                        let l = !1, c = (0, i.query)(".art-progress-hover", e),
                            u = (0, i.query)(".art-progress-loaded", e), p = (0, i.query)(".art-progress-played", e),
                            d = (0, i.query)(".art-progress-highlight", e),
                            f = (0, i.query)(".art-progress-indicator", e), h = (0, i.query)(".art-progress-tip", e);

                        function m(e, t) {
                            "loaded" === e && (0, i.setStyle)(u, "width", `${100 * t}%`), "played" === e && ((0, i.setStyle)(p, "width", `${100 * t}%`), (0, i.setStyle)(f, "left", `${100 * t}%`))
                        }

                        r.indicator ? (0, i.append)(f, r.indicator) : (0, i.setStyle)(f, "backgroundColor", "var(--art-theme)"), t.on("video:loadedmetadata", () => {
                            for (let e = 0; e < a.highlight.length; e++) {
                                let r = a.highlight[e], o = (0, i.clamp)(r.time, 0, t.duration) / t.duration * 100,
                                    n = `<span data-text="${r.text}" data-time="${r.time}" style="left: ${o}%"></span>`;
                                (0, i.append)(d, n)
                            }
                        }), m("loaded", t.loaded), t.on("setBar", (e, t) => {
                            m(e, t)
                        }), t.on("video:progress", () => {
                            m("loaded", t.loaded)
                        }), t.constructor.USE_RAF ? t.on("raf", () => {
                            m("played", t.played)
                        }) : t.on("video:timeupdate", () => {
                            m("played", t.played)
                        }), t.on("video:ended", () => {
                            m("played", 1)
                        }), i.isMobile || (s(e, "click", e => {
                            e.target !== f && n(t, e)
                        }), s(e, "mousemove", r => {
                            (function (e) {
                                let {width: r} = o(t, e);
                                (0, i.setStyle)(c, "width", `${r}px`), (0, i.setStyle)(c, "display", "flex")
                            })(r), (0, i.setStyle)(h, "display", "flex"), (0, i.includeFromEvent)(r, d) ? function (r) {
                                let {width: a} = o(t, r), {text: n} = r.target.dataset;
                                h.innerHTML = n;
                                let s = h.clientWidth;
                                a <= s / 2 ? (0, i.setStyle)(h, "left", 0) : a > e.clientWidth - s / 2 ? (0, i.setStyle)(h, "left", `${e.clientWidth - s}px`) : (0, i.setStyle)(h, "left", `${a - s / 2}px`)
                            }(r) : function (r) {
                                let {width: a, time: n} = o(t, r);
                                h.innerHTML = n;
                                let s = h.clientWidth;
                                a <= s / 2 ? (0, i.setStyle)(h, "left", 0) : a > e.clientWidth - s / 2 ? (0, i.setStyle)(h, "left", `${e.clientWidth - s}px`) : (0, i.setStyle)(h, "left", `${a - s / 2}px`)
                            }(r)
                        }), s(e, "mouseleave", () => {
                            (0, i.setStyle)(h, "display", "none"), (0, i.setStyle)(c, "display", "none")
                        }), s(e, "mousedown", e => {
                            l = 0 === e.button
                        }), t.on("document:mousemove", e => {
                            if (l) {
                                let {second: r, percentage: a} = o(t, e);
                                m("played", a), t.seek = r
                            }
                        }), t.on("document:mouseup", () => {
                            l && (l = !1)
                        }))
                    }
                }
            }
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "7H0CE": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            return t => ({
                ...e,
                style: i.isMobile ? {fontSize: "12px", padding: "0 5px"} : {cursor: "auto", padding: "0 10px"},
                mounted: e => {
                    function r() {
                        let r = `${(0, i.secondToTime)(t.currentTime)} / ${(0, i.secondToTime)(t.duration)}`;
                        r !== e.innerText && (e.innerText = r)
                    }

                    r();
                    let a = ["video:loadedmetadata", "video:timeupdate", "video:progress"];
                    for (let e = 0; e < a.length; e++) t.on(a[e], r)
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    lMwFm: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            return t => ({
                ...e, mounted: e => {
                    let {proxy: r, icons: a} = t, o = (0, i.append)(e, a.volume), n = (0, i.append)(e, a.volumeClose),
                        s = (0, i.append)(e, '<div class="art-volume-panel"></div>'),
                        l = (0, i.append)(s, '<div class="art-volume-inner"></div>'),
                        c = (0, i.append)(l, '<div class="art-volume-val"></div>'),
                        u = (0, i.append)(l, '<div class="art-volume-slider"></div>'),
                        p = (0, i.append)(u, '<div class="art-volume-handle"></div>'),
                        d = (0, i.append)(p, '<div class="art-volume-loaded"></div>'),
                        f = (0, i.append)(u, '<div class="art-volume-indicator"></div>');

                    function h(e) {
                        let {top: t, height: r} = u.getBoundingClientRect();
                        return 1 - (e.clientY - t) / r
                    }

                    function m() {
                        if (t.muted || 0 === t.volume) (0, i.setStyle)(o, "display", "none"), (0, i.setStyle)(n, "display", "flex"), (0, i.setStyle)(f, "top", "100%"), (0, i.setStyle)(d, "top", "100%"), c.innerText = 0; else {
                            let e = 100 * t.volume;
                            (0, i.setStyle)(o, "display", "flex"), (0, i.setStyle)(n, "display", "none"), (0, i.setStyle)(f, "top", `${100 - e}%`), (0, i.setStyle)(d, "top", `${100 - e}%`), c.innerText = Math.floor(e)
                        }
                    }

                    if (m(), t.on("video:volumechange", m), r(o, "click", () => {
                        t.muted = !0
                    }), r(n, "click", () => {
                        t.muted = !1
                    }), i.isMobile) (0, i.setStyle)(s, "display", "none"); else {
                        let e = !1;
                        r(u, "mousedown", r => {
                            e = 0 === r.button, t.volume = h(r)
                        }), t.on("document:mousemove", r => {
                            e && (t.muted = !1, t.volume = h(r))
                        }), t.on("document:mouseup", () => {
                            e && (e = !1)
                        })
                    }
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "8BrCu": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            return t => ({
                ...e, tooltip: t.i18n.get("Show Setting"), mounted: e => {
                    let {proxy: r, icons: a, i18n: o} = t;
                    (0, i.append)(e, a.setting), r(e, "click", () => {
                        t.setting.toggle(), t.setting.updateStyle()
                    }), t.on("setting", t => {
                        (0, i.tooltip)(e, o.get(t ? "Hide Setting" : "Show Setting"))
                    })
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "2HiWx": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => n);
        var i = e("../utils"), o = e("./progress");

        function n(e) {
            return t => ({
                ...e, mounted: e => {
                    let {option: r, template: {$progress: a, $video: n}, events: {proxy: s, loadImg: l}} = t, c = null,
                        u = !1, p = !1, d = !1;
                    s(a, "mousemove", async s => {
                        d = !0, u || (u = !0, c = await l(r.thumbnails.url), p = !0), p && d && ((0, i.setStyle)(e, "display", "flex"), function (s) {
                            let {width: l} = (0, o.getPosFromEvent)(t, s), {
                                    url: u,
                                    number: p,
                                    column: d,
                                    width: f,
                                    height: h
                                } = r.thumbnails, m = f || c.naturalWidth / d, g = h || m / (n.videoWidth / n.videoHeight),
                                v = Math.floor(l / (a.clientWidth / p)), y = Math.ceil(v / d) - 1;
                            (0, i.setStyle)(e, "backgroundImage", `url(${u})`), (0, i.setStyle)(e, "height", `${g}px`), (0, i.setStyle)(e, "width", `${m}px`), (0, i.setStyle)(e, "backgroundPosition", `-${(v % d || d - 1) * m}px -${y * g}px`), l <= m / 2 ? (0, i.setStyle)(e, "left", 0) : l > a.clientWidth - m / 2 ? (0, i.setStyle)(e, "left", `${a.clientWidth - m}px`) : (0, i.setStyle)(e, "left", `${l - m / 2}px`)
                        }(s))
                    }), s(a, "mouseleave", () => {
                        d = !1, (0, i.setStyle)(e, "display", "none")
                    }), t.on("hover", t => {
                        t || (0, i.setStyle)(e, "display", "none")
                    })
                }
            })
        }
    }, {"../utils": "h3rH9", "./progress": "aBBSH", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    c1GeG: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            return t => ({
                ...e, tooltip: t.i18n.get("Screenshot"), mounted: e => {
                    let {proxy: r, icons: a} = t;
                    (0, i.append)(e, a.screenshot), r(e, "click", () => {
                        t.screenshot()
                    })
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "6GRju": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            return t => ({
                ...e, tooltip: t.i18n.get("AirPlay"), mounted: e => {
                    let {proxy: r, icons: a} = t;
                    (0, i.append)(e, a.airplay), r(e, "click", () => t.airplay())
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "7iUum": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("../utils"), o = e("../utils/component"), n = a.interopDefault(o), s = e("./playbackRate"),
            l = a.interopDefault(s), c = e("./aspectRatio"), u = a.interopDefault(c), p = e("./flip"),
            d = a.interopDefault(p), f = e("./info"), h = a.interopDefault(f), m = e("./version"),
            g = a.interopDefault(m), v = e("./close"), y = a.interopDefault(v);

        class b extends n.default {
            constructor(e) {
                super(e), this.name = "contextmenu", this.$parent = e.template.$contextmenu, i.isMobile || this.init()
            }

            init() {
                let {option: e, proxy: t, template: {$player: r, $contextmenu: a}} = this.art;
                e.playbackRate && this.add((0, l.default)({
                    name: "playbackRate",
                    index: 10
                })), e.aspectRatio && this.add((0, u.default)({
                    name: "aspectRatio",
                    index: 20
                })), e.flip && this.add((0, d.default)({
                    name: "flip",
                    index: 30
                })), this.add((0, h.default)({name: "info", index: 40})), this.add((0, g.default)({
                    name: "version",
                    index: 50
                })), this.add((0, y.default)({name: "close", index: 60}));
                for (let t = 0; t < e.contextmenu.length; t++) this.add(e.contextmenu[t]);
                t(r, "contextmenu", e => {
                    if (e.preventDefault(), !this.art.constructor.CONTEXTMENU) return;
                    this.show = !0;
                    let t = e.clientX, o = e.clientY, {
                            height: n,
                            width: s,
                            left: l,
                            top: c
                        } = r.getBoundingClientRect(), {height: u, width: p} = a.getBoundingClientRect(), d = t - l,
                        f = o - c;
                    t + p > l + s && (d = s - p), o + u > c + n && (f = n - u), (0, i.setStyles)(a, {
                        top: `${f}px`,
                        left: `${d}px`
                    })
                }), t(r, "click", e => {
                    (0, i.includeFromEvent)(e, a) || (this.show = !1)
                }), this.art.on("blur", () => {
                    this.show = !1
                })
            }
        }

        r.default = b
    }, {
        "../utils": "h3rH9",
        "../utils/component": "guki8",
        "./playbackRate": "f1W36",
        "./aspectRatio": "afxZC",
        "./flip": "9jCuX",
        "./info": "k8wIZ",
        "./version": "bb0TU",
        "./close": "9zTkI",
        "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"
    }],
    f1W36: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            return t => {
                let {i18n: r, constructor: {PLAYBACK_RATE: a}} = t,
                    o = a.map(e => `<span data-value="${e}">${1 === e ? r.get("Normal") : e.toFixed(1)}</span>`).join("");
                return {
                    ...e, html: `${r.get("Play Speed")}: ${o}`, click: (e, r) => {
                        let {value: a} = r.target.dataset;
                        a && (t.playbackRate = Number(a), e.show = !1)
                    }, mounted: e => {
                        let r = (0, i.query)('[data-value="1"]', e);
                        r && (0, i.inverseClass)(r, "art-current"), t.on("video:ratechange", () => {
                            let r = (0, i.queryAll)("span", e).find(e => Number(e.dataset.value) === t.playbackRate);
                            r && (0, i.inverseClass)(r, "art-current")
                        })
                    }
                }
            }
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    afxZC: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            return t => {
                let {i18n: r, constructor: {ASPECT_RATIO: a}} = t,
                    o = a.map(e => `<span data-value="${e}">${"default" === e ? r.get("Default") : e}</span>`).join("");
                return {
                    ...e, html: `${r.get("Aspect Ratio")}: ${o}`, click: (e, r) => {
                        let {value: a} = r.target.dataset;
                        a && (t.aspectRatio = a, e.show = !1)
                    }, mounted: e => {
                        let r = (0, i.query)('[data-value="default"]', e);
                        r && (0, i.inverseClass)(r, "art-current"), t.on("aspectRatio", t => {
                            let r = (0, i.queryAll)("span", e).find(e => e.dataset.value === t);
                            r && (0, i.inverseClass)(r, "art-current")
                        })
                    }
                }
            }
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "9jCuX": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            return t => {
                let {i18n: r, constructor: {FLIP: a}} = t,
                    o = a.map(e => `<span data-value="${e}">${r.get((0, i.capitalize)(e))}</span>`).join("");
                return {
                    ...e, html: `${r.get("Video Flip")}: ${o}`, click: (e, r) => {
                        let {value: a} = r.target.dataset;
                        a && (t.flip = a.toLowerCase(), e.show = !1)
                    }, mounted: e => {
                        let r = (0, i.query)('[data-value="normal"]', e);
                        r && (0, i.inverseClass)(r, "art-current"), t.on("flip", t => {
                            let r = (0, i.queryAll)("span", e).find(e => e.dataset.value === t);
                            r && (0, i.inverseClass)(r, "art-current")
                        })
                    }
                }
            }
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    k8wIZ: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");

        function i(e) {
            return t => ({
                ...e, html: t.i18n.get("Video Info"), click: e => {
                    t.info.show = !0, e.show = !1
                }
            })
        }

        a.defineInteropFlag(r), a.export(r, "default", () => i)
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    bb0TU: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");

        function i(e) {
            return {...e, html: '<a href="https://artplayer.org" target="_blank">ArtPlayer 5.1.2</a>'}
        }

        a.defineInteropFlag(r), a.export(r, "default", () => i)
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "9zTkI": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");

        function i(e) {
            return t => ({
                ...e, html: t.i18n.get("Close"), click: e => {
                    e.show = !1
                }
            })
        }

        a.defineInteropFlag(r), a.export(r, "default", () => i)
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    hD2Lg: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("./utils"), o = e("./utils/component"), n = a.interopDefault(o);

        class s extends n.default {
            constructor(e) {
                super(e), this.name = "info", i.isMobile || this.init()
            }

            init() {
                let {proxy: e, constructor: t, template: {$infoPanel: r, $infoClose: a, $video: o}} = this.art;
                e(a, "click", () => {
                    this.show = !1
                });
                let n = null, s = (0, i.queryAll)("[data-video]", r) || [];
                this.art.on("destroy", () => clearTimeout(n)), function e() {
                    for (let e = 0; e < s.length; e++) {
                        let t = s[e], r = o[t.dataset.video], a = "number" == typeof r ? r.toFixed(2) : r;
                        t.innerText !== a && (t.innerText = a)
                    }
                    n = setTimeout(e, t.INFO_LOOP_TIME)
                }()
            }
        }

        r.default = s
    }, {"./utils": "h3rH9", "./utils/component": "guki8", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    lum0D: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("./utils"), o = e("./utils/component"), n = a.interopDefault(o), s = e("option-validator"),
            l = a.interopDefault(s), c = e("./scheme"), u = a.interopDefault(c);

        class p extends n.default {
            constructor(e) {
                super(e), this.name = "subtitle", this.eventDestroy = () => null, this.init(e.option.subtitle);
                let t = !1;
                e.on("video:timeupdate", () => {
                    if (!this.url) return;
                    let e = this.art.template.$video.webkitDisplayingFullscreen;
                    "boolean" == typeof e && e !== t && (t = e, this.createTrack(e ? "subtitles" : "metadata", this.url))
                })
            }

            get url() {
                return this.art.template.$track.src
            }

            set url(e) {
                this.switch(e)
            }

            get textTrack() {
                return this.art.template.$video.textTracks[0]
            }

            get activeCue() {
                return this.textTrack.activeCues[0]
            }

            style(e, t) {
                let {$subtitle: r} = this.art.template;
                return "object" == typeof e ? (0, i.setStyles)(r, e) : (0, i.setStyle)(r, e, t)
            }

            update() {
                let {$subtitle: e} = this.art.template;
                e.innerHTML = "", this.activeCue && (this.art.option.subtitle.escape ? e.innerHTML = this.activeCue.text.split(/\r?\n/).map(e => `<div class="art-subtitle-line">${(0, i.escape)(e)}</div>`).join("") : e.innerHTML = this.activeCue.text, this.art.emit("subtitleUpdate", this.activeCue.text))
            }

            async switch(e, t = {}) {
                let {i18n: r, notice: a, option: i} = this.art, o = {...i.subtitle, ...t, url: e},
                    n = await this.init(o);
                return t.name && (a.show = `${r.get("Switch Subtitle")}: ${t.name}`), n
            }

            createTrack(e, t) {
                let {template: r, proxy: a, option: o} = this.art, {$video: n, $track: s} = r,
                    l = (0, i.createElement)("track");
                l.default = !0, l.kind = e, l.src = t, l.label = o.subtitle.name || "Artplayer", l.track.mode = "hidden", this.eventDestroy(), (0, i.remove)(s), (0, i.append)(n, l), r.$track = l, this.eventDestroy = a(this.textTrack, "cuechange", () => this.update())
            }

            async init(e) {
                let {notice: t, template: {$subtitle: r}} = this.art;
                if ((0, l.default)(e, u.default.subtitle), e.url) return this.style(e.style), fetch(e.url).then(e => e.arrayBuffer()).then(t => {
                    let r = new TextDecoder(e.encoding).decode(t);
                    switch (this.art.emit("subtitleLoad", e.url), e.type || (0, i.getExt)(e.url)) {
                        case"srt": {
                            let t = (0, i.srtToVtt)(r), a = e.onVttLoad(t);
                            return (0, i.vttToBlob)(a)
                        }
                        case"ass": {
                            let t = (0, i.assToVtt)(r), a = e.onVttLoad(t);
                            return (0, i.vttToBlob)(a)
                        }
                        case"vtt": {
                            let t = e.onVttLoad(r);
                            return (0, i.vttToBlob)(t)
                        }
                        default:
                            return e.url
                    }
                }).then(e => (r.innerHTML = "", this.url === e || (URL.revokeObjectURL(this.url), this.createTrack("metadata", e), this.art.emit("subtitleSwitch", e)), e)).catch(e => {
                    throw r.innerHTML = "", t.show = e, e
                })
            }
        }

        r.default = p
    }, {
        "./utils": "h3rH9",
        "./utils/component": "guki8",
        "option-validator": "9I0i9",
        "./scheme": "AdvwB",
        "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"
    }],
    "1Epl5": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("../utils/error"), o = e("./clickInit"), n = a.interopDefault(o), s = e("./hoverInit"),
            l = a.interopDefault(s), c = e("./moveInit"), u = a.interopDefault(c), p = e("./resizeInit"),
            d = a.interopDefault(p), f = e("./gestureInit"), h = a.interopDefault(f), m = e("./viewInit"),
            g = a.interopDefault(m), v = e("./documentInit"), y = a.interopDefault(v), b = e("./updateInit"),
            x = a.interopDefault(b);
        r.default = class {
            constructor(e) {
                this.destroyEvents = [], this.proxy = this.proxy.bind(this), this.hover = this.hover.bind(this), this.loadImg = this.loadImg.bind(this), (0, n.default)(e, this), (0, l.default)(e, this), (0, u.default)(e, this), (0, d.default)(e, this), (0, h.default)(e, this), (0, g.default)(e, this), (0, y.default)(e, this), (0, x.default)(e, this)
            }

            proxy(e, t, r, a = {}) {
                if (Array.isArray(t)) return t.map(t => this.proxy(e, t, r, a));
                e.addEventListener(t, r, a);
                let i = () => e.removeEventListener(t, r, a);
                return this.destroyEvents.push(i), i
            }

            hover(e, t, r) {
                t && this.proxy(e, "mouseenter", t), r && this.proxy(e, "mouseleave", r)
            }

            loadImg(e) {
                return new Promise((t, r) => {
                    let a;
                    if (e instanceof HTMLImageElement) a = e; else {
                        if ("string" != typeof e) return r(new i.ArtPlayerError("Unable to get Image"));
                        (a = new Image).src = e
                    }
                    if (a.complete) return t(a);
                    this.proxy(a, "load", () => t(a)), this.proxy(a, "error", () => r(new i.ArtPlayerError(`Failed to load Image: ${a.src}`)))
                })
            }

            remove(e) {
                let t = this.destroyEvents.indexOf(e);
                t > -1 && (e(), this.destroyEvents.splice(t, 1))
            }

            destroy() {
                for (let e = 0; e < this.destroyEvents.length; e++) this.destroyEvents[e]()
            }
        }
    }, {
        "../utils/error": "2nFlF",
        "./clickInit": "gzL6e",
        "./hoverInit": "kpTJf",
        "./moveInit": "ef6qz",
        "./resizeInit": "9TXOX",
        "./gestureInit": "dePMU",
        "./viewInit": "hDyWF",
        "./documentInit": "7RjDP",
        "./updateInit": "8SmBT",
        "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"
    }],
    gzL6e: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e, t) {
            let {constructor: r, template: {$player: a, $video: o}} = e;
            t.proxy(document, ["click", "contextmenu"], t => {
                (0, i.includeFromEvent)(t, a) ? (e.isInput = "INPUT" === t.target.tagName, e.isFocus = !0, e.emit("focus", t)) : (e.isInput = !1, e.isFocus = !1, e.emit("blur", t))
            });
            let n = [];
            t.proxy(o, "click", t => {
                let a = Date.now();
                n.push(a);
                let {MOBILE_CLICK_PLAY: o, DBCLICK_TIME: s, MOBILE_DBCLICK_PLAY: l, DBCLICK_FULLSCREEN: c} = r,
                    u = n.filter(e => a - e <= s);
                switch (u.length) {
                    case 1:
                        e.emit("click", t), i.isMobile ? !e.isLock && o && e.toggle() : e.toggle(), n = u;
                        break;
                    case 2:
                        e.emit("dblclick", t), i.isMobile ? !e.isLock && l && e.toggle() : c && (e.fullscreen = !e.fullscreen), n = [];
                        break;
                    default:
                        n = []
                }
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    kpTJf: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e, t) {
            let {$player: r} = e.template;
            t.hover(r, t => {
                (0, i.addClass)(r, "art-hover"), e.emit("hover", !0, t)
            }, t => {
                (0, i.removeClass)(r, "art-hover"), e.emit("hover", !1, t)
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    ef6qz: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");

        function i(e, t) {
            let {$player: r} = e.template;
            t.proxy(r, "mousemove", t => {
                e.emit("mousemove", t)
            })
        }

        a.defineInteropFlag(r), a.export(r, "default", () => i)
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "9TXOX": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e, t) {
            let {option: r, constructor: a} = e;
            e.on("resize", () => {
                let {aspectRatio: t, notice: a} = e;
                "standard" === e.state && r.autoSize && e.autoSize(), e.aspectRatio = t, a.show = ""
            });
            let o = (0, i.debounce)(() => e.emit("resize"), a.RESIZE_TIME);
            t.proxy(window, ["orientationchange", "resize"], () => o()), screen && screen.orientation && screen.orientation.onchange && t.proxy(screen.orientation, "change", () => o())
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    dePMU: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => n);
        var i = e("../utils"), o = e("../control/progress");

        function n(e, t) {
            if (i.isMobile && !e.option.isLive) {
                let {$video: r, $progress: a} = e.template, n = null, s = !1, l = 0, c = 0, u = 0, p = t => {
                    if (1 === t.touches.length && !e.isLock) {
                        n === a && (0, o.setCurrentTime)(e, t), s = !0;
                        let {pageX: r, pageY: i} = t.touches[0];
                        l = r, c = i, u = e.currentTime
                    }
                }, d = t => {
                    if (1 === t.touches.length && s && e.duration) {
                        let {pageX: a, pageY: o} = t.touches[0], s = function (e, t, r, a) {
                            var i = t - a, o = r - e, n = 0;
                            if (2 > Math.abs(o) && 2 > Math.abs(i)) return n;
                            var s = 180 * Math.atan2(i, o) / Math.PI;
                            return s >= -45 && s < 45 ? n = 4 : s >= 45 && s < 135 ? n = 1 : s >= -135 && s < -45 ? n = 2 : (s >= 135 && s <= 180 || s >= -180 && s < -135) && (n = 3), n
                        }(l, c, a, o), p = [3, 4].includes(s), d = [1, 2].includes(s);
                        if (p && !e.isRotate || d && e.isRotate) {
                            let t = (0, i.clamp)((a - l) / e.width, -1, 1), s = (0, i.clamp)((o - c) / e.height, -1, 1),
                                p = e.isRotate ? s : t, d = n === r ? e.constructor.TOUCH_MOVE_RATIO : 1,
                                f = (0, i.clamp)(u + e.duration * p * d, 0, e.duration);
                            e.seek = f, e.emit("setBar", "played", (0, i.clamp)(f / e.duration, 0, 1)), e.notice.show = `${(0, i.secondToTime)(f)} / ${(0, i.secondToTime)(e.duration)}`
                        }
                    }
                };
                t.proxy(a, "touchstart", e => {
                    n = a, p(e)
                }), t.proxy(r, "touchstart", e => {
                    n = r, p(e)
                }), t.proxy(r, "touchmove", d), t.proxy(a, "touchmove", d), t.proxy(document, "touchend", () => {
                    s && (l = 0, c = 0, u = 0, s = !1, n = null)
                })
            }
        }
    }, {
        "../utils": "h3rH9",
        "../control/progress": "aBBSH",
        "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"
    }],
    hDyWF: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e, t) {
            let {option: r, constructor: a, template: {$container: o}} = e, n = (0, i.throttle)(() => {
                e.emit("view", (0, i.isInViewport)(o, a.SCROLL_GAP))
            }, a.SCROLL_TIME);
            t.proxy(window, "scroll", () => n()), e.on("view", t => {
                r.autoMini && (e.mini = !t)
            })
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "7RjDP": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");

        function i(e, t) {
            t.proxy(document, "mousemove", t => {
                e.emit("document:mousemove", t)
            }), t.proxy(document, "mouseup", t => {
                e.emit("document:mouseup", t)
            })
        }

        a.defineInteropFlag(r), a.export(r, "default", () => i)
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "8SmBT": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");

        function i(e) {
            if (e.constructor.USE_RAF) {
                let t = null;
                !function r() {
                    e.playing && e.emit("raf"), e.isDestroy || (t = requestAnimationFrame(r))
                }(), e.on("destroy", () => {
                    cancelAnimationFrame(t)
                })
            }
        }

        a.defineInteropFlag(r), a.export(r, "default", () => i)
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    eTow4: [function (e, t, r) {
        e("@parcel/transformer-js/src/esmodule-helpers.js").defineInteropFlag(r);
        var a = e("./utils");
        r.default = class {
            constructor(e) {
                this.art = e, this.keys = {}, e.option.hotkey && !a.isMobile && this.init()
            }

            init() {
                let {proxy: e, constructor: t} = this.art;
                this.add(27, () => {
                    this.art.fullscreenWeb && (this.art.fullscreenWeb = !1)
                }), this.add(32, () => {
                    this.art.toggle()
                }), this.add(37, () => {
                    this.art.backward = t.SEEK_STEP
                }), this.add(38, () => {
                    this.art.volume += t.VOLUME_STEP
                }), this.add(39, () => {
                    //this.art.forward = t.SEEK_STEP
                }), this.add(40, () => {
                    this.art.volume -= t.VOLUME_STEP
                }), e(window, "keydown", e => {
                    if (this.art.isFocus) {
                        let t = document.activeElement.tagName.toUpperCase(),
                            r = document.activeElement.getAttribute("contenteditable");
                        if ("INPUT" !== t && "TEXTAREA" !== t && "" !== r && "true" !== r && !e.altKey && !e.ctrlKey && !e.metaKey && !e.shiftKey) {
                            let t = this.keys[e.keyCode];
                            if (t) {
                                e.preventDefault();
                                for (let r = 0; r < t.length; r++) t[r].call(this.art, e);
                                this.art.emit("hotkey", e)
                            }
                        }
                    }
                })
            }

            add(e, t) {
                return this.keys[e] ? this.keys[e].push(t) : this.keys[e] = [t], this
            }

            remove(e, t) {
                if (this.keys[e]) {
                    let r = this.keys[e].indexOf(t);
                    -1 !== r && this.keys[e].splice(r, 1)
                }
                return this
            }
        }
    }, {"./utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "4fDoD": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("./utils/component"), o = a.interopDefault(i);

        class n extends o.default {
            constructor(e) {
                super(e);
                let {option: t, template: {$layer: r}} = e;
                this.name = "layer", this.$parent = r;
                for (let e = 0; e < t.layers.length; e++) this.add(t.layers[e])
            }
        }

        r.default = n
    }, {"./utils/component": "guki8", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    fE0Sp: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("./utils"), o = e("./utils/component"), n = a.interopDefault(o);

        class s extends n.default {
            constructor(e) {
                super(e), this.name = "loading", (0, i.append)(e.template.$loading, e.icons.loading)
            }
        }

        r.default = s
    }, {"./utils": "h3rH9", "./utils/component": "guki8", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "9PuGy": [function (e, t, r) {
        e("@parcel/transformer-js/src/esmodule-helpers.js").defineInteropFlag(r);
        var a = e("./utils");
        r.default = class {
            constructor(e) {
                this.art = e, this.timer = null
            }

            set show(e) {
                let {constructor: t, template: {$player: r, $noticeInner: i}} = this.art;
                e ? (i.innerText = e instanceof Error ? e.message.trim() : e, (0, a.addClass)(r, "art-notice-show"), clearTimeout(this.timer), this.timer = setTimeout(() => {
                    i.innerText = "", (0, a.removeClass)(r, "art-notice-show")
                }, t.NOTICE_TIME)) : (0, a.removeClass)(r, "art-notice-show")
            }
        }
    }, {"./utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "2etr0": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("./utils"), o = e("./utils/component"), n = a.interopDefault(o);

        class s extends n.default {
            constructor(e) {
                super(e), this.name = "mask";
                let {template: t, icons: r, events: a} = e, o = (0, i.append)(t.$state, r.state),
                    n = (0, i.append)(t.$state, r.error);
                (0, i.setStyle)(n, "display", "none"), e.on("destroy", () => {
                    (0, i.setStyle)(o, "display", "none"), (0, i.setStyle)(n, "display", null)
                }), a.proxy(t.$state, "click", () => e.play())
            }
        }

        r.default = s
    }, {"./utils": "h3rH9", "./utils/component": "guki8", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "6dYSr": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("../utils"), o = e("bundle-text:./loading.svg"), n = a.interopDefault(o),
            s = e("bundle-text:./state.svg"), l = a.interopDefault(s), c = e("bundle-text:./check.svg"),
            u = a.interopDefault(c), p = e("bundle-text:./play.svg"), d = a.interopDefault(p),
            f = e("bundle-text:./pause.svg"), h = a.interopDefault(f), m = e("bundle-text:./volume.svg"),
            g = a.interopDefault(m), v = e("bundle-text:./volume-close.svg"), y = a.interopDefault(v),
            b = e("bundle-text:./screenshot.svg"), x = a.interopDefault(b), w = e("bundle-text:./setting.svg"),
            j = a.interopDefault(w), k = e("bundle-text:./arrow-left.svg"), S = a.interopDefault(k),
            I = e("bundle-text:./arrow-right.svg"), T = a.interopDefault(I), O = e("bundle-text:./playback-rate.svg"),
            E = a.interopDefault(O), M = e("bundle-text:./aspect-ratio.svg"), $ = a.interopDefault(M),
            F = e("bundle-text:./config.svg"), C = a.interopDefault(F), H = e("bundle-text:./pip.svg"),
            D = a.interopDefault(H), B = e("bundle-text:./lock.svg"), z = a.interopDefault(B),
            A = e("bundle-text:./unlock.svg"), R = a.interopDefault(A), L = e("bundle-text:./fullscreen-off.svg"),
            P = a.interopDefault(L), N = e("bundle-text:./fullscreen-on.svg"), Z = a.interopDefault(N),
            _ = e("bundle-text:./fullscreen-web-off.svg"), q = a.interopDefault(_),
            V = e("bundle-text:./fullscreen-web-on.svg"), W = a.interopDefault(V), U = e("bundle-text:./switch-on.svg"),
            Y = a.interopDefault(U), K = e("bundle-text:./switch-off.svg"), X = a.interopDefault(K),
            G = e("bundle-text:./flip.svg"), J = a.interopDefault(G), Q = e("bundle-text:./error.svg"),
            ee = a.interopDefault(Q), et = e("bundle-text:./close.svg"), er = a.interopDefault(et),
            ea = e("bundle-text:./airplay.svg"), ei = a.interopDefault(ea);
        r.default = class {
            constructor(e) {
                let t = {
                    loading: n.default,
                    state: l.default,
                    play: d.default,
                    pause: h.default,
                    check: u.default,
                    volume: g.default,
                    volumeClose: y.default,
                    screenshot: x.default,
                    setting: j.default,
                    pip: D.default,
                    arrowLeft: S.default,
                    arrowRight: T.default,
                    playbackRate: E.default,
                    aspectRatio: $.default,
                    config: C.default,
                    lock: z.default,
                    flip: J.default,
                    unlock: R.default,
                    fullscreenOff: P.default,
                    fullscreenOn: Z.default,
                    fullscreenWebOff: q.default,
                    fullscreenWebOn: W.default,
                    switchOn: Y.default,
                    switchOff: X.default,
                    error: ee.default,
                    close: er.default,
                    airplay: ei.default, ...e.option.icons
                };
                for (let e in t) (0, i.def)(this, e, {get: () => (0, i.getIcon)(e, t[e])})
            }
        }
    }, {
        "../utils": "h3rH9",
        "bundle-text:./loading.svg": "fY5Gt",
        "bundle-text:./state.svg": "iNfLt",
        "bundle-text:./check.svg": "jtE9u",
        "bundle-text:./play.svg": "elgfY",
        "bundle-text:./pause.svg": "eKokJ",
        "bundle-text:./volume.svg": "hNB4y",
        "bundle-text:./volume-close.svg": "i9vta",
        "bundle-text:./screenshot.svg": "kB3Mf",
        "bundle-text:./setting.svg": "3MONs",
        "bundle-text:./arrow-left.svg": "iMCpk",
        "bundle-text:./arrow-right.svg": "3oe4L",
        "bundle-text:./playback-rate.svg": "liE22",
        "bundle-text:./aspect-ratio.svg": "8HqYc",
        "bundle-text:./config.svg": "hYAAH",
        "bundle-text:./pip.svg": "jmNrH",
        "bundle-text:./lock.svg": "cIqko",
        "bundle-text:./unlock.svg": "65zy4",
        "bundle-text:./fullscreen-off.svg": "jaJRT",
        "bundle-text:./fullscreen-on.svg": "cRY1X",
        "bundle-text:./fullscreen-web-off.svg": "3aVGL",
        "bundle-text:./fullscreen-web-on.svg": "4DiVn",
        "bundle-text:./switch-on.svg": "kwdKE",
        "bundle-text:./switch-off.svg": "bWfXZ",
        "bundle-text:./flip.svg": "h3zZ9",
        "bundle-text:./error.svg": "7Oyth",
        "bundle-text:./close.svg": "U5Jcy",
        "bundle-text:./airplay.svg": "jK5Fx",
        "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"
    }],
    fY5Gt: [function (e, t, r) {
        t.exports = '<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" viewBox="0 0 100 100" preserveAspectRatio="xMidYMid" class="uil-default"><path fill="none" class="bk" d="M0 0h100v100H0z"/><rect x="47" y="40" width="6" height="20" rx="5" ry="5" fill="#fff" transform="translate(0 -30)"><animate attributeName="opacity" from="1" to="0" dur="1s" begin="-1s" repeatCount="indefinite"/></rect><rect x="47" y="40" width="6" height="20" rx="5" ry="5" fill="#fff" transform="rotate(30 105.98 65)"><animate attributeName="opacity" from="1" to="0" dur="1s" begin="-0.9166666666666666s" repeatCount="indefinite"/></rect><rect x="47" y="40" width="6" height="20" rx="5" ry="5" fill="#fff" transform="rotate(60 75.98 65)"><animate attributeName="opacity" from="1" to="0" dur="1s" begin="-0.8333333333333334s" repeatCount="indefinite"/></rect><rect x="47" y="40" width="6" height="20" rx="5" ry="5" fill="#fff" transform="rotate(90 65 65)"><animate attributeName="opacity" from="1" to="0" dur="1s" begin="-0.75s" repeatCount="indefinite"/></rect><rect x="47" y="40" width="6" height="20" rx="5" ry="5" fill="#fff" transform="rotate(120 58.66 65)"><animate attributeName="opacity" from="1" to="0" dur="1s" begin="-0.6666666666666666s" repeatCount="indefinite"/></rect><rect x="47" y="40" width="6" height="20" rx="5" ry="5" fill="#fff" transform="rotate(150 54.02 65)"><animate attributeName="opacity" from="1" to="0" dur="1s" begin="-0.5833333333333334s" repeatCount="indefinite"/></rect><rect x="47" y="40" width="6" height="20" rx="5" ry="5" fill="#fff" transform="rotate(180 50 65)"><animate attributeName="opacity" from="1" to="0" dur="1s" begin="-0.5s" repeatCount="indefinite"/></rect><rect x="47" y="40" width="6" height="20" rx="5" ry="5" fill="#fff" transform="rotate(-150 45.98 65)"><animate attributeName="opacity" from="1" to="0" dur="1s" begin="-0.4166666666666667s" repeatCount="indefinite"/></rect><rect x="47" y="40" width="6" height="20" rx="5" ry="5" fill="#fff" transform="rotate(-120 41.34 65)"><animate attributeName="opacity" from="1" to="0" dur="1s" begin="-0.3333333333333333s" repeatCount="indefinite"/></rect><rect x="47" y="40" width="6" height="20" rx="5" ry="5" fill="#fff" transform="rotate(-90 35 65)"><animate attributeName="opacity" from="1" to="0" dur="1s" begin="-0.25s" repeatCount="indefinite"/></rect><rect x="47" y="40" width="6" height="20" rx="5" ry="5" fill="#fff" transform="rotate(-60 24.02 65)"><animate attributeName="opacity" from="1" to="0" dur="1s" begin="-0.16666666666666666s" repeatCount="indefinite"/></rect><rect x="47" y="40" width="6" height="20" rx="5" ry="5" fill="#fff" transform="rotate(-30 -5.98 65)"><animate attributeName="opacity" from="1" to="0" dur="1s" begin="-0.08333333333333333s" repeatCount="indefinite"/></rect></svg>'
    }, {}],
    iNfLt: [function (e, t, r) {
        t.exports = '<svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" viewBox="0 0 24 24"><path fill="#fff" d="M9.5 9.325v5.35q0 .575.525.875t1.025-.05l4.15-2.65q.475-.3.475-.85t-.475-.85L11.05 8.5q-.5-.35-1.025-.05t-.525.875ZM12 22q-2.075 0-3.9-.788t-3.175-2.137q-1.35-1.35-2.137-3.175T2 12q0-2.075.788-3.9t2.137-3.175q1.35-1.35 3.175-2.137T12 2q2.075 0 3.9.788t3.175 2.137q1.35 1.35 2.138 3.175T22 12q0 2.075-.788 3.9t-2.137 3.175q-1.35 1.35-3.175 2.138T12 22Z"/></svg>'
    }, {}],
    jtE9u: [function (e, t, r) {
        t.exports = '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" style="width:100%;height:100%"><path d="M9 16.2 4.8 12l-1.4 1.4L9 19 21 7l-1.4-1.4L9 16.2z" fill="#fff"/></svg>'
    }, {}],
    elgfY: [function (e, t, r) {
        t.exports = '<svg xmlns="http://www.w3.org/2000/svg" height="22" width="22"><path d="M17.982 9.275 8.06 3.27A2.013 2.013 0 0 0 5 4.994v12.011a2.017 2.017 0 0 0 3.06 1.725l9.922-6.005a2.017 2.017 0 0 0 0-3.45z"/></svg>'
    }, {}],
    eKokJ: [function (e, t, r) {
        t.exports = '<svg xmlns="http://www.w3.org/2000/svg" height="22" width="22"><path d="M7 3a2 2 0 0 0-2 2v12a2 2 0 1 0 4 0V5a2 2 0 0 0-2-2zm8 0a2 2 0 0 0-2 2v12a2 2 0 1 0 4 0V5a2 2 0 0 0-2-2z"/></svg>'
    }, {}],
    hNB4y: [function (e, t, r) {
        t.exports = '<svg xmlns="http://www.w3.org/2000/svg" height="22" width="22"><path d="M10.188 4.65 6 8H5a2 2 0 0 0-2 2v2a2 2 0 0 0 2 2h1l4.188 3.35a.5.5 0 0 0 .812-.39V5.04a.498.498 0 0 0-.812-.39zm4.258-.872a1 1 0 0 0-.862 1.804 6.002 6.002 0 0 1-.007 10.838 1 1 0 0 0 .86 1.806A8.001 8.001 0 0 0 19 11a8.001 8.001 0 0 0-4.554-7.222z"/><path d="M15 11a3.998 3.998 0 0 0-2-3.465v6.93A3.998 3.998 0 0 0 15 11z"/></svg>'
    }, {}],
    i9vta: [function (e, t, r) {
        t.exports = '<svg xmlns="http://www.w3.org/2000/svg" height="22" width="22"><path d="M15 11a3.998 3.998 0 0 0-2-3.465v2.636l1.865 1.865A4.02 4.02 0 0 0 15 11z"/><path d="M13.583 5.583A5.998 5.998 0 0 1 17 11a6 6 0 0 1-.585 2.587l1.477 1.477a8.001 8.001 0 0 0-3.446-11.286 1 1 0 0 0-.863 1.805zm5.195 13.195-2.121-2.121-1.414-1.414-1.415-1.415L13 13l-2-2-3.889-3.889-3.889-3.889a.999.999 0 1 0-1.414 1.414L5.172 8H5a2 2 0 0 0-2 2v2a2 2 0 0 0 2 2h1l4.188 3.35a.5.5 0 0 0 .812-.39v-3.131l2.587 2.587-.01.005a1 1 0 0 0 .86 1.806c.215-.102.424-.214.627-.333l2.3 2.3a1.001 1.001 0 0 0 1.414-1.416zM11 5.04a.5.5 0 0 0-.813-.39L8.682 5.854 11 8.172V5.04z"/></svg>'
    }, {}],
    kB3Mf: [function (e, t, r) {
        t.exports = '<svg xmlns="http://www.w3.org/2000/svg" height="22" width="22" viewBox="0 0 50 50"><path d="M19.402 6a5 5 0 0 0-4.902 4.012L14.098 12H9a5 5 0 0 0-5 5v21a5 5 0 0 0 5 5h32a5 5 0 0 0 5-5V17a5 5 0 0 0-5-5h-5.098l-.402-1.988A5 5 0 0 0 30.598 6ZM25 17c5.52 0 10 4.48 10 10s-4.48 10-10 10-10-4.48-10-10 4.48-10 10-10Zm0 2c-4.41 0-8 3.59-8 8s3.59 8 8 8 8-3.59 8-8-3.59-8-8-8Z"/></svg>'
    }, {}],
    "3MONs": [function (e, t, r) {
        t.exports = '<svg xmlns="http://www.w3.org/2000/svg" height="22" width="22"><circle cx="11" cy="11" r="2"/><path d="M19.164 8.861 17.6 8.6a6.978 6.978 0 0 0-1.186-2.099l.574-1.533a1 1 0 0 0-.436-1.217l-1.997-1.153a1.001 1.001 0 0 0-1.272.23l-1.008 1.225a7.04 7.04 0 0 0-2.55.001L8.716 2.829a1 1 0 0 0-1.272-.23L5.447 3.751a1 1 0 0 0-.436 1.217l.574 1.533A6.997 6.997 0 0 0 4.4 8.6l-1.564.261A.999.999 0 0 0 2 9.847v2.306c0 .489.353.906.836.986l1.613.269a7 7 0 0 0 1.228 2.075l-.558 1.487a1 1 0 0 0 .436 1.217l1.997 1.153c.423.244.961.147 1.272-.23l1.04-1.263a7.089 7.089 0 0 0 2.272 0l1.04 1.263a1 1 0 0 0 1.272.23l1.997-1.153a1 1 0 0 0 .436-1.217l-.557-1.487c.521-.61.94-1.31 1.228-2.075l1.613-.269a.999.999 0 0 0 .835-.986V9.847a.999.999 0 0 0-.836-.986zM11 15a4 4 0 1 1 0-8 4 4 0 0 1 0 8z"/></svg>'
    }, {}],
    iMCpk: [function (e, t, r) {
        t.exports = '<svg xmlns="http://www.w3.org/2000/svg" height="32" width="32"><path d="m19.41 20.09-4.58-4.59 4.58-4.59L18 9.5l-6 6 6 6z" fill="#fff"/></svg>'
    }, {}],
    "3oe4L": [function (e, t, r) {
        t.exports = '<svg xmlns="http://www.w3.org/2000/svg" height="32" width="32"><path d="m12.59 20.34 4.58-4.59-4.58-4.59L14 9.75l6 6-6 6z" fill="#fff"/></svg>'
    }, {}],
    liE22: [function (e, t, r) {
        t.exports = '<svg height="24" width="24"><path d="M10 8v8l6-4-6-4zM6.3 5l-.6-.8C7.2 3 9 2.2 11 2l.1 1c-1.8.2-3.4.9-4.8 2zM5 6.3l-.8-.6C3 7.2 2.2 9 2 11l1 .1c.2-1.8.9-3.4 2-4.8zm0 11.4c-1.1-1.4-1.8-3.1-2-4.8L2 13c.2 2 1 3.8 2.2 5.4l.8-.7zm6.1 3.3c-1.8-.2-3.4-.9-4.8-2l-.6.8C7.2 21 9 21.8 11 22l.1-1zM22 12c0-5.2-3.9-9.4-9-10l-.1 1c4.6.5 8.1 4.3 8.1 9s-3.5 8.5-8.1 9l.1 1c5.2-.5 9-4.8 9-10z" fill="#fff" style="--darkreader-inline-fill:#a8a6a4"/></svg>'
    }, {}],
    "8HqYc": [function (e, t, r) {
        t.exports = '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 88 88" style="width:100%;height:100%;transform:translate(0,0)"><defs><clipPath id="__lottie_element_216"><path d="M0 0h88v88H0z"/></clipPath></defs><g style="display:block" clip-path="url(\'#__lottie_element_216\')"><path fill="#FFF" d="m12.438-12.702-2.82 2.82c-.79.79-.79 2.05 0 2.83l7.07 7.07-7.07 7.07c-.79.79-.79 2.05 0 2.83l2.82 2.83c.79.78 2.05.78 2.83 0l11.32-11.31c.78-.78.78-2.05 0-2.83l-11.32-11.31c-.78-.79-2.04-.79-2.83 0zm-24.88 0c-.74-.74-1.92-.78-2.7-.12l-.13.12-11.31 11.31a2 2 0 0 0-.12 2.7l.12.13 11.31 11.31a2 2 0 0 0 2.7.12l.13-.12 2.83-2.83c.74-.74.78-1.91.11-2.7l-.11-.13-7.07-7.07 7.07-7.07c.74-.74.78-1.91.11-2.7l-.11-.13-2.83-2.82zM28-28c4.42 0 8 3.58 8 8v40c0 4.42-3.58 8-8 8h-56c-4.42 0-8-3.58-8-8v-40c0-4.42 3.58-8 8-8h56z" style="--darkreader-inline-fill:#a8a6a4" transform="translate(44 44)"/></g></svg>'
    }, {}],
    hYAAH: [function (e, t, r) {
        t.exports = '<svg height="24" width="24"><path d="M15 17h6v1h-6v-1zm-4 0H3v1h8v2h1v-5h-1v2zm3-9h1V3h-1v2H3v1h11v2zm4-3v1h3V5h-3zM6 14h1V9H6v2H3v1h3v2zm4-2h11v-1H10v1z" fill="#fff" style="--darkreader-inline-fill:#a8a6a4"/></svg>'
    }, {}],
    jmNrH: [function (e, t, r) {
        t.exports = '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 36 36" height="32" width="32"><path d="M25 17h-8v6h8v-6Zm4 8V10.98C29 9.88 28.1 9 27 9H9c-1.1 0-2 .88-2 1.98V25c0 1.1.9 2 2 2h18c1.1 0 2-.9 2-2Zm-2 .02H9V10.97h18v14.05Z"/></svg>'
    }, {}],
    cIqko: [function (e, t, r) {
        t.exports = '<svg class="icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="20" height="20"><path d="M298.667 426.667v-85.334a213.333 213.333 0 1 1 426.666 0v85.334H768A85.333 85.333 0 0 1 853.333 512v256A85.333 85.333 0 0 1 768 853.333H256A85.333 85.333 0 0 1 170.667 768V512A85.333 85.333 0 0 1 256 426.667h42.667zM512 213.333a128 128 0 0 0-128 128v85.334h256v-85.334a128 128 0 0 0-128-128z" fill="#fff"/></svg>'
    }, {}],
    "65zy4": [function (e, t, r) {
        t.exports = '<svg class="icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="20" height="20"><path d="m666.752 194.517-49.365 74.112A128 128 0 0 0 384 341.333l.043 85.334h384A85.333 85.333 0 0 1 853.376 512v256a85.333 85.333 0 0 1-85.333 85.333H256A85.333 85.333 0 0 1 170.667 768V512A85.333 85.333 0 0 1 256 426.667h42.667v-85.334a213.333 213.333 0 0 1 368.085-146.816z" fill="#fff"/></svg>'
    }, {}],
    jaJRT: [function (e, t, r) {
        t.exports = '<svg class="icon" width="22" height="22" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="#fff" d="M768 298.667h170.667V384h-256V128H768v170.667zM341.333 384h-256v-85.333H256V128h85.333v256zM768 725.333V896h-85.333V640h256v85.333H768zM341.333 640v256H256V725.333H85.333V640h256z"/></svg>'
    }, {}],
    cRY1X: [function (e, t, r) {
        t.exports = '<svg class="icon" width="22" height="22" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="#fff" d="M625.778 256H768v142.222h113.778v-256h-256V256zM256 398.222V256h142.222V142.222h-256v256H256zm512 227.556V768H625.778v113.778h256v-256H768zM398.222 768H256V625.778H142.222v256h256V768z"/></svg>'
    }, {}],
    "3aVGL": [function (e, t, r) {
        t.exports = '<svg class="icon" width="18" height="18" viewBox="0 0 1152 1024" xmlns="http://www.w3.org/2000/svg"><path fill="#fff" d="M1075.2 0H76.8A76.8 76.8 0 0 0 0 76.8v870.4a76.8 76.8 0 0 0 76.8 76.8h998.4a76.8 76.8 0 0 0 76.8-76.8V76.8A76.8 76.8 0 0 0 1075.2 0zM1024 128v768H128V128h896zM896 512a64 64 0 0 1 7.488 127.552L896 640H768v128a64 64 0 0 1-56.512 63.552L704 832a64 64 0 0 1-63.552-56.512L640 768V582.592c0-34.496 25.024-66.112 61.632-70.208l8-.384H896zm-640 0a64 64 0 0 1-7.488-127.552L256 384h128V256a64 64 0 0 1 56.512-63.552L448 192a64 64 0 0 1 63.552 56.512L512 256v185.408c0 34.432-25.024 66.112-61.632 70.144l-8 .448H256z"/></svg>'
    }, {}],
    "4DiVn": [function (e, t, r) {
        t.exports = '<svg class="icon" width="18" height="18" viewBox="0 0 1152 1024" xmlns="http://www.w3.org/2000/svg"><path fill="#fff" d="M1075.2 0H76.8A76.8 76.8 0 0 0 0 76.8v870.4a76.8 76.8 0 0 0 76.8 76.8h998.4a76.8 76.8 0 0 0 76.8-76.8V76.8A76.8 76.8 0 0 0 1075.2 0zM1024 128v768H128V128h896zm-576 64a64 64 0 0 1 7.488 127.552L448 320H320v128a64 64 0 0 1-56.512 63.552L256 512a64 64 0 0 1-63.552-56.512L192 448V262.592c0-34.432 25.024-66.112 61.632-70.144l8-.448H448zm256 640a64 64 0 0 1-7.488-127.552L704 704h128V576a64 64 0 0 1 56.512-63.552L896 512a64 64 0 0 1 63.552 56.512L960 576v185.408c0 34.496-25.024 66.112-61.632 70.208l-8 .384H704z"/></svg>'
    }, {}],
    kwdKE: [function (e, t, r) {
        t.exports = '<svg class="icon" width="26" height="26" viewBox="0 0 1664 1024" xmlns="http://www.w3.org/2000/svg"><path fill="#648FFC" d="M1152 0H512a512 512 0 0 0 0 1024h640a512 512 0 0 0 0-1024zm0 960a448 448 0 1 1 448-448 448 448 0 0 1-448 448z"/></svg>'
    }, {}],
    bWfXZ: [function (e, t, r) {
        t.exports = '<svg class="icon" width="26" height="26" viewBox="0 0 1740 1024" xmlns="http://www.w3.org/2000/svg"><path fill="#fff" d="M511.898 1024h670.515c282.419-.41 511.18-229.478 511.18-511.898 0-282.419-228.761-511.488-511.18-511.897H511.898C229.478.615.717 229.683.717 512.102c0 282.42 228.761 511.488 511.18 511.898zm-.564-975.36A464.589 464.589 0 1 1 48.026 513.024 463.872 463.872 0 0 1 511.334 48.435v.205z"/></svg>'
    }, {}],
    h3zZ9: [function (e, t, r) {
        t.exports = '<svg class="icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="24" height="24"><path d="M554.667 810.667V896h-85.334v-85.333h85.334zm-384-632.662a42.667 42.667 0 0 1 34.986 18.219l203.904 291.328a42.667 42.667 0 0 1 0 48.896L205.611 827.776A42.667 42.667 0 0 1 128 803.328V220.672a42.667 42.667 0 0 1 42.667-42.667zm682.666 0a42.667 42.667 0 0 1 42.368 37.718l.299 4.949v582.656a42.667 42.667 0 0 1-74.24 28.63l-3.413-4.182-203.904-291.328a42.667 42.667 0 0 1-3.03-43.861l3.03-5.035 203.946-291.328a42.667 42.667 0 0 1 34.944-18.219zM554.667 640v85.333h-85.334V640h85.334zm-358.4-320.896V716.8L335.957 512 196.31 319.104zm358.4 150.23v85.333h-85.334v-85.334h85.334zm0-170.667V384h-85.334v-85.333h85.334zm0-170.667v85.333h-85.334V128h85.334z" fill="#fff"/></svg>'
    }, {}],
    "7Oyth": [function (e, t, r) {
        t.exports = '<svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="50" height="50"><path d="M593.818 168.55 949.82 763.76c26.153 43.746 10.732 99.738-34.447 125.052-14.397 8.069-30.72 12.308-47.37 12.308H155.976c-52.224 0-94.536-40.96-94.536-91.505 0-16.097 4.383-31.928 12.718-45.875l356.004-595.19c26.173-43.724 84.009-58.654 129.208-33.341a93.082 93.082 0 0 1 34.448 33.341zM512 819.2a61.44 61.44 0 1 0 0-122.88 61.44 61.44 0 0 0 0 122.88zm0-512a72.315 72.315 0 0 0-71.762 81.306l25.723 205.721a46.408 46.408 0 0 0 92.078 0l25.723-205.742A72.315 72.315 0 0 0 512 307.2z"/></svg>'
    }, {}],
    U5Jcy: [function (e, t, r) {
        t.exports = '<svg class="icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="22" height="22"><path d="m571.733 512 268.8-268.8c17.067-17.067 17.067-42.667 0-59.733-17.066-17.067-42.666-17.067-59.733 0L512 452.267l-268.8-268.8c-17.067-17.067-42.667-17.067-59.733 0-17.067 17.066-17.067 42.666 0 59.733l268.8 268.8-268.8 268.8c-17.067 17.067-17.067 42.667 0 59.733 8.533 8.534 19.2 12.8 29.866 12.8s21.334-4.266 29.867-12.8l268.8-268.8 268.8 268.8c8.533 8.534 19.2 12.8 29.867 12.8s21.333-4.266 29.866-12.8c17.067-17.066 17.067-42.666 0-59.733L571.733 512z"/></svg>'
    }, {}],
    jK5Fx: [function (e, t, r) {
        t.exports = '<svg width="18" height="18" xmlns="http://www.w3.org/2000/svg"><g fill="#fff"><path d="M16 1H2a1 1 0 0 0-1 1v10a1 1 0 0 0 1 1h3v-2H3V3h12v8h-2v2h3a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1Z"/><path d="M4 17h10l-5-6z"/></g></svg>'
    }, {}],
    bRHiA: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("./flip"), o = a.interopDefault(i), n = e("./aspectRatio"), s = a.interopDefault(n),
            l = e("./playbackRate"), c = a.interopDefault(l), u = e("./subtitleOffset"), p = a.interopDefault(u),
            d = e("../utils/component"), f = a.interopDefault(d), h = e("../utils/error"), m = e("../utils");

        class g extends f.default {
            constructor(e) {
                super(e);
                let {option: t, controls: r, template: {$setting: a}} = e;
                this.name = "setting", this.$parent = a, this.option = [], this.events = [], this.cache = new Map, t.setting && (this.init(), e.on("blur", () => {
                    this.show && (this.show = !1, this.render(this.option))
                }), e.on("focus", e => {
                    let t = (0, m.includeFromEvent)(e, r.setting), a = (0, m.includeFromEvent)(e, this.$parent);
                    !this.show || t || a || (this.show = !1, this.render(this.option))
                }))
            }

            static makeRecursion(e, t, r) {
                for (let a = 0; a < e.length; a++) {
                    let i = e[a];
                    i.$parentItem = t, i.$parentList = r, g.makeRecursion(i.selector || [], i, e)
                }
                return e
            }

            get defaultSettings() {
                let e = [], {option: t} = this.art;
                return t.playbackRate && e.push((0, c.default)(this.art)), t.aspectRatio && e.push((0, s.default)(this.art)), t.flip && e.push((0, o.default)(this.art)), t.subtitleOffset && e.push((0, p.default)(this.art)), e
            }

            init() {
                let {option: e} = this.art, t = [...this.defaultSettings, ...e.settings];
                this.option = g.makeRecursion(t), this.destroy(), this.render(this.option)
            }

            destroy() {
                for (let e = 0; e < this.events.length; e++) this.art.events.remove(this.events[e]);
                this.$parent.innerHTML = "", this.events = [], this.cache = new Map
            }

            find(e = "", t = this.option) {
                for (let r = 0; r < t.length; r++) {
                    let a = t[r];
                    if (a.name === e) return a;
                    {
                        let t = this.find(e, a.selector || []);
                        if (t) return t
                    }
                }
            }

            remove(e) {
                let t = this.find(e);
                (0, h.errorHandle)(t, `Can't find [${e}] from the [setting]`);
                let r = t.$parentItem ? t.$parentItem.selector : this.option;
                return r.splice(r.indexOf(t), 1), this.option = g.makeRecursion(this.option), this.destroy(), this.render(this.option), this.option
            }

            update(e) {
                let t = this.find(e.name);
                return t ? (Object.assign(t, e), this.option = g.makeRecursion(this.option), this.destroy(), this.render(this.option)) : this.add(e), this.option
            }

            add(e) {
                return this.option.push(e), this.option = g.makeRecursion(this.option), this.destroy(), this.render(this.option), this.option
            }

            creatHeader(e) {
                let {icons: t, proxy: r, constructor: a} = this.art, i = (0, m.createElement)("div");
                (0, m.setStyle)(i, "height", `${a.SETTING_ITEM_HEIGHT}px`), (0, m.addClass)(i, "art-setting-item"), (0, m.addClass)(i, "art-setting-item-back");
                let o = (0, m.append)(i, '<div class="art-setting-item-left"></div>'), n = (0, m.createElement)("div");
                (0, m.addClass)(n, "art-setting-item-left-icon"), (0, m.append)(n, t.arrowLeft), (0, m.append)(o, n), (0, m.append)(o, e.$parentItem.html);
                let s = r(i, "click", () => this.render(e.$parentList));
                return this.events.push(s), i
            }

            creatItem(e, t) {
                let {icons: r, proxy: a, constructor: i} = this.art, o = (0, m.createElement)("div");
                (0, m.addClass)(o, "art-setting-item"), (0, m.setStyle)(o, "height", `${i.SETTING_ITEM_HEIGHT}px`), (0, m.isStringOrNumber)(t.name) && (o.dataset.name = t.name), (0, m.isStringOrNumber)(t.value) && (o.dataset.value = t.value);
                let n = (0, m.append)(o, '<div class="art-setting-item-left"></div>'),
                    s = (0, m.append)(o, '<div class="art-setting-item-right"></div>'), l = (0, m.createElement)("div");
                switch ((0, m.addClass)(l, "art-setting-item-left-icon"), e) {
                    case"switch":
                    case"range":
                        (0, m.append)(l, (0, m.isStringOrNumber)(t.icon) || t.icon instanceof Element ? t.icon : r.config);
                        break;
                    case"selector":
                        t.selector && t.selector.length ? (0, m.append)(l, (0, m.isStringOrNumber)(t.icon) || t.icon instanceof Element ? t.icon : r.config) : (0, m.append)(l, r.check)
                }
                (0, m.append)(n, l), t.$icon = l, (0, m.def)(t, "icon", {
                    configurable: !0,
                    get: () => l.innerHTML,
                    set(e) {
                        (0, m.isStringOrNumber)(e) && (l.innerHTML = e)
                    }
                });
                let c = (0, m.createElement)("div");
                (0, m.addClass)(c, "art-setting-item-left-text"), (0, m.append)(c, t.html || ""), (0, m.append)(n, c), t.$html = c, (0, m.def)(t, "html", {
                    configurable: !0,
                    get: () => c.innerHTML,
                    set(e) {
                        (0, m.isStringOrNumber)(e) && (c.innerHTML = e)
                    }
                });
                let u = (0, m.createElement)("div");
                switch ((0, m.addClass)(u, "art-setting-item-right-tooltip"), (0, m.append)(u, t.tooltip || ""), (0, m.append)(s, u), t.$tooltip = u, (0, m.def)(t, "tooltip", {
                    configurable: !0,
                    get: () => u.innerHTML,
                    set(e) {
                        (0, m.isStringOrNumber)(e) && (u.innerHTML = e)
                    }
                }), e) {
                    case"switch": {
                        let e = (0, m.createElement)("div");
                        (0, m.addClass)(e, "art-setting-item-right-icon");
                        let a = (0, m.append)(e, r.switchOn), i = (0, m.append)(e, r.switchOff);
                        (0, m.setStyle)(t.switch ? i : a, "display", "none"), (0, m.append)(s, e), t.$switch = t.switch, (0, m.def)(t, "switch", {
                            configurable: !0,
                            get: () => t.$switch,
                            set(e) {
                                t.$switch = e, e ? ((0, m.setStyle)(i, "display", "none"), (0, m.setStyle)(a, "display", null)) : ((0, m.setStyle)(i, "display", null), (0, m.setStyle)(a, "display", "none"))
                            }
                        });
                        break
                    }
                    case"range": {
                        let e = (0, m.createElement)("div");
                        (0, m.addClass)(e, "art-setting-item-right-icon");
                        let r = (0, m.append)(e, '<input type="range">');
                        r.value = t.range[0] || 0, r.min = t.range[1] || 0, r.max = t.range[2] || 10, r.step = t.range[3] || 1, (0, m.addClass)(r, "art-setting-range"), (0, m.append)(s, e), t.$range = r, (0, m.def)(t, "range", {
                            configurable: !0,
                            get: () => r.valueAsNumber,
                            set(e) {
                                r.value = Number(e)
                            }
                        })
                    }
                        break;
                    case"selector":
                        if (t.selector && t.selector.length) {
                            let e = (0, m.createElement)("div");
                            (0, m.addClass)(e, "art-setting-item-right-icon"), (0, m.append)(e, r.arrowRight), (0, m.append)(s, e)
                        }
                }
                switch (e) {
                    case"switch":
                        if (t.onSwitch) {
                            let e = a(o, "click", async e => {
                                t.switch = await t.onSwitch.call(this.art, t, o, e)
                            });
                            this.events.push(e)
                        }
                        break;
                    case"range":
                        if (t.$range) {
                            if (t.onRange) {
                                let e = a(t.$range, "change", async e => {
                                    t.tooltip = await t.onRange.call(this.art, t, o, e)
                                });
                                this.events.push(e)
                            }
                            if (t.onChange) {
                                let e = a(t.$range, "input", async e => {
                                    t.tooltip = await t.onChange.call(this.art, t, o, e)
                                });
                                this.events.push(e)
                            }
                        }
                        break;
                    case"selector": {
                        let e = a(o, "click", async e => {
                            if (t.selector && t.selector.length) this.render(t.selector, t.width); else {
                                (0, m.inverseClass)(o, "art-current");
                                for (let e = 0; e < t.$parentItem.selector.length; e++) {
                                    let r = t.$parentItem.selector[e];
                                    r.default = r === t
                                }
                                if (t.$parentList && this.render(t.$parentList), t.$parentItem && t.$parentItem.onSelect) {
                                    let r = await t.$parentItem.onSelect.call(this.art, t, o, e);
                                    t.$parentItem.$tooltip && (0, m.isStringOrNumber)(r) && (t.$parentItem.$tooltip.innerHTML = r)
                                }
                            }
                        });
                        this.events.push(e), t.default && (0, m.addClass)(o, "art-current")
                    }
                }
                return o
            }

            updateStyle(e) {
                let {controls: t, constructor: r, template: {$player: a, $setting: i}} = this.art;
                if (t.setting && !m.isMobile) {
                    let o = e || r.SETTING_WIDTH, {left: n, width: s} = t.setting.getBoundingClientRect(), {
                        left: l,
                        width: c
                    } = a.getBoundingClientRect(), u = n - l + s / 2 - o / 2;
                    u + o > c ? ((0, m.setStyle)(i, "left", null), (0, m.setStyle)(i, "right", null)) : ((0, m.setStyle)(i, "left", `${u}px`), (0, m.setStyle)(i, "right", "auto"))
                }
            }

            render(e, t) {
                let {constructor: r} = this.art;
                if (this.cache.has(e)) {
                    let t = this.cache.get(e);
                    (0, m.inverseClass)(t, "art-current"), (0, m.setStyle)(this.$parent, "width", `${t.dataset.width}px`), (0, m.setStyle)(this.$parent, "height", `${t.dataset.height}px`), this.updateStyle(Number(t.dataset.width))
                } else {
                    let a = (0, m.createElement)("div");
                    (0, m.addClass)(a, "art-setting-panel"), a.dataset.width = t || r.SETTING_WIDTH, a.dataset.height = e.length * r.SETTING_ITEM_HEIGHT, e[0] && e[0].$parentItem && ((0, m.append)(a, this.creatHeader(e[0])), a.dataset.height = Number(a.dataset.height) + r.SETTING_ITEM_HEIGHT);
                    for (let t = 0; t < e.length; t++) {
                        let r = e[t];
                        (0, m.has)(r, "switch") ? (0, m.append)(a, this.creatItem("switch", r)) : (0, m.has)(r, "range") ? (0, m.append)(a, this.creatItem("range", r)) : (0, m.append)(a, this.creatItem("selector", r))
                    }
                    (0, m.append)(this.$parent, a), this.cache.set(e, a), (0, m.inverseClass)(a, "art-current"), (0, m.setStyle)(this.$parent, "width", `${a.dataset.width}px`), (0, m.setStyle)(this.$parent, "height", `${a.dataset.height}px`), this.updateStyle(Number(a.dataset.width)), e[0] && e[0].$parentItem && e[0].$parentItem.mounted && e[0].$parentItem.mounted.call(this.art, a, e[0].$parentItem)
                }
            }
        }

        r.default = g
    }, {
        "./flip": "bNOaj",
        "./aspectRatio": "5lAsp",
        "./playbackRate": "e6hsR",
        "./subtitleOffset": "fFNEr",
        "../utils/component": "guki8",
        "../utils/error": "2nFlF",
        "../utils": "h3rH9",
        "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"
    }],
    bNOaj: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {i18n: t, icons: r, constructor: {SETTING_ITEM_WIDTH: a, FLIP: o}} = e;

            function n(e, r, a) {
                r && (r.innerText = t.get((0, i.capitalize)(a)));
                let o = (0, i.queryAll)(".art-setting-item", e).find(e => e.dataset.value === a);
                o && (0, i.inverseClass)(o, "art-current")
            }

            return {
                width: a,
                name: "flip",
                html: t.get("Video Flip"),
                tooltip: t.get((0, i.capitalize)(e.flip)),
                icon: r.flip,
                selector: o.map(r => ({
                    value: r,
                    name: `aspect-ratio-${r}`,
                    default: r === e.flip,
                    html: t.get((0, i.capitalize)(r))
                })),
                onSelect: t => (e.flip = t.value, t.html),
                mounted: (t, r) => {
                    n(t, r.$tooltip, e.flip), e.on("flip", () => {
                        n(t, r.$tooltip, e.flip)
                    })
                }
            }
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "5lAsp": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {i18n: t, icons: r, constructor: {SETTING_ITEM_WIDTH: a, ASPECT_RATIO: o}} = e;

            function n(e) {
                return "default" === e ? t.get("Default") : e
            }

            function s(e, t, r) {
                t && (t.innerText = n(r));
                let a = (0, i.queryAll)(".art-setting-item", e).find(e => e.dataset.value === r);
                a && (0, i.inverseClass)(a, "art-current")
            }

            return {
                width: a,
                name: "aspect-ratio",
                html: t.get("Aspect Ratio"),
                icon: r.aspectRatio,
                tooltip: n(e.aspectRatio),
                selector: o.map(t => ({value: t, name: `aspect-ratio-${t}`, default: t === e.aspectRatio, html: n(t)})),
                onSelect: t => (e.aspectRatio = t.value, t.html),
                mounted: (t, r) => {
                    s(t, r.$tooltip, e.aspectRatio), e.on("aspectRatio", () => {
                        s(t, r.$tooltip, e.aspectRatio)
                    })
                }
            }
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    e6hsR: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {i18n: t, icons: r, constructor: {SETTING_ITEM_WIDTH: a, PLAYBACK_RATE: o}} = e;

            function n(e) {
                return 1 === e ? t.get("Normal") : e.toFixed(1)
            }

            function s(e, t, r) {
                t && (t.innerText = n(r));
                let a = (0, i.queryAll)(".art-setting-item", e).find(e => Number(e.dataset.value) === r);
                a && (0, i.inverseClass)(a, "art-current")
            }

            return {
                width: a,
                name: "playback-rate",
                html: t.get("Play Speed"),
                tooltip: n(e.playbackRate),
                icon: r.playbackRate,
                selector: o.map(t => ({
                    value: t,
                    name: `aspect-ratio-${t}`,
                    default: t === e.playbackRate,
                    html: n(t)
                })),
                onSelect: t => (e.playbackRate = t.value, t.html),
                mounted: (t, r) => {
                    s(t, r.$tooltip, e.playbackRate), e.on("video:ratechange", () => {
                        s(t, r.$tooltip, e.playbackRate)
                    })
                }
            }
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    fFNEr: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");

        function i(e) {
            let {i18n: t, icons: r, constructor: a} = e;
            return {
                width: a.SETTING_ITEM_WIDTH,
                name: "subtitle-offset",
                html: t.get("Subtitle Offset"),
                icon: r.subtitle,
                tooltip: "0s",
                range: [0, -5, 5, .1],
                onChange: t => (e.subtitleOffset = t.range, t.range + "s")
            }
        }

        a.defineInteropFlag(r), a.export(r, "default", () => i)
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    f2Thp: [function (e, t, r) {
        e("@parcel/transformer-js/src/esmodule-helpers.js").defineInteropFlag(r), r.default = class {
            constructor() {
                this.name = "artplayer_settings", this.settings = {}
            }

            get(e) {
                try {
                    let t = JSON.parse(window.localStorage.getItem(this.name)) || {};
                    return e ? t[e] : t
                } catch (t) {
                    return e ? this.settings[e] : this.settings
                }
            }

            set(e, t) {
                try {
                    let r = Object.assign({}, this.get(), {[e]: t});
                    window.localStorage.setItem(this.name, JSON.stringify(r))
                } catch (r) {
                    this.settings[e] = t
                }
            }

            del(e) {
                try {
                    let t = this.get();
                    delete t[e], window.localStorage.setItem(this.name, JSON.stringify(t))
                } catch (t) {
                    delete this.settings[e]
                }
            }

            clear() {
                try {
                    window.localStorage.removeItem(this.name)
                } catch (e) {
                    this.settings = {}
                }
            }
        }
    }, {"@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "96ThS": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r);
        var i = e("../utils"), o = e("./miniProgressBar"), n = a.interopDefault(o), s = e("./autoOrientation"),
            l = a.interopDefault(s), c = e("./autoPlayback"), u = a.interopDefault(c), p = e("./fastForward"),
            d = a.interopDefault(p), f = e("./lock"), h = a.interopDefault(f);
        r.default = class {
            constructor(e) {
                this.art = e, this.id = 0;
                let {option: t} = e;
                t.miniProgressBar && !t.isLive && this.add(n.default), t.lock && i.isMobile && this.add(h.default), t.autoPlayback && !t.isLive && this.add(u.default), t.autoOrientation && i.isMobile && this.add(l.default), t.fastForward && i.isMobile && !t.isLive && this.add(d.default);
                for (let e = 0; e < t.plugins.length; e++) this.add(t.plugins[e])
            }

            add(e) {
                this.id += 1;
                let t = e.call(this.art, this.art);
                return t instanceof Promise ? t.then(t => this.next(e, t)) : this.next(e, t)
            }

            next(e, t) {
                let r = t && t.name || e.name || `plugin${this.id}`;
                return (0, i.errorHandle)(!(0, i.has)(this, r), `Cannot add a plugin that already has the same name: ${r}`), (0, i.def)(this, r, {value: t}), this
            }
        }
    }, {
        "../utils": "h3rH9",
        "./miniProgressBar": "iBx4M",
        "./autoOrientation": "2O9qO",
        "./autoPlayback": "iiOc1",
        "./fastForward": "d9NUE",
        "./lock": "5dnKh",
        "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"
    }],
    iBx4M: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            return e.on("control", t => {
                t ? (0, i.removeClass)(e.template.$player, "art-mini-progress-bar") : (0, i.addClass)(e.template.$player, "art-mini-progress-bar")
            }), {name: "mini-progress-bar"}
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "2O9qO": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {constructor: t, template: {$player: r, $video: a}} = e;
            return e.on("fullscreenWeb", o => {
                if (o) {
                    let {videoWidth: o, videoHeight: n} = a, {
                        clientWidth: s,
                        clientHeight: l
                    } = document.documentElement;
                    (o > n && s < l || o < n && s > l) && setTimeout(() => {
                        (0, i.setStyle)(r, "width", `${l}px`), (0, i.setStyle)(r, "height", `${s}px`), (0, i.setStyle)(r, "transform-origin", "0 0"), (0, i.setStyle)(r, "transform", `rotate(90deg) translate(0, -${s}px)`), (0, i.addClass)(r, "art-auto-orientation"), e.isRotate = !0, e.emit("resize")
                    }, t.AUTO_ORIENTATION_TIME)
                } else (0, i.hasClass)(r, "art-auto-orientation") && ((0, i.removeClass)(r, "art-auto-orientation"), e.isRotate = !1, e.emit("resize"))
            }), e.on("fullscreen", async e => {
                let t = screen.orientation.type;
                if (e) {
                    let {videoWidth: e, videoHeight: o} = a, {
                        clientWidth: n,
                        clientHeight: s
                    } = document.documentElement;
                    if (e > o && n < s || e < o && n > s) {
                        let e = t.startsWith("portrait") ? "landscape" : "portrait";
                        await screen.orientation.lock(e), (0, i.addClass)(r, "art-auto-orientation-fullscreen")
                    }
                } else (0, i.hasClass)(r, "art-auto-orientation-fullscreen") && (await screen.orientation.lock(t), (0, i.removeClass)(r, "art-auto-orientation-fullscreen"))
            }), {
                name: "autoOrientation", get state() {
                    return (0, i.hasClass)(r, "art-auto-orientation")
                }
            }
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    iiOc1: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {i18n: t, icons: r, storage: a, constructor: o, proxy: n, template: {$poster: s}} = e,
                l = e.layers.add({
                    name: "auto-playback",
                    html: `<div class="art-auto-playback-close"></div><div class="art-auto-playback-last"></div><div class="art-auto-playback-jump"></div>`
                }), c = (0, i.query)(".art-auto-playback-last", l), u = (0, i.query)(".art-auto-playback-jump", l),
                p = (0, i.query)(".art-auto-playback-close", l);
            return e.on("video:timeupdate", () => {
                if (e.playing) {
                    let t = a.get("times") || {}, r = Object.keys(t);
                    r.length > o.AUTO_PLAYBACK_MAX && delete t[r[0]], t[e.option.id || e.option.url] = e.currentTime, a.set("times", t)
                }
            }), e.on("ready", () => {
                let d = (a.get("times") || {})[e.option.id || e.option.url];
                d && d >= o.AUTO_PLAYBACK_MIN && ((0, i.append)(p, r.close), (0, i.setStyle)(l, "display", "flex"), c.innerText = `${t.get("Last Seen")} ${(0, i.secondToTime)(d)}`, u.innerText = t.get("Jump Play"), n(p, "click", () => {
                    (0, i.setStyle)(l, "display", "none")
                }), n(u, "click", () => {
                    e.seek = d, e.play(), (0, i.setStyle)(s, "display", "none"), (0, i.setStyle)(l, "display", "none")
                }), e.once("video:timeupdate", () => {
                    setTimeout(() => {
                        (0, i.setStyle)(l, "display", "none")
                    }, o.AUTO_PLAYBACK_TIMEOUT)
                }))
            }), {
                name: "auto-playback", get times() {
                    return a.get("times") || {}
                }, clear: () => a.del("times"), delete(e) {
                    let t = a.get("times") || {};
                    return delete t[e], a.set("times", t), t
                }
            }
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    d9NUE: [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {constructor: t, proxy: r, template: {$player: a, $video: o}} = e, n = null, s = !1, l = 1, c = () => {
                clearTimeout(n), s && (s = !1, e.playbackRate = l, (0, i.removeClass)(a, "art-fast-forward"))
            };
            return r(o, "touchstart", r => {
                1 === r.touches.length && e.playing && !e.isLock && (n = setTimeout(() => {
                    s = !0, l = e.playbackRate, e.playbackRate = t.FAST_FORWARD_VALUE, (0, i.addClass)(a, "art-fast-forward")
                }, t.FAST_FORWARD_TIME))
            }), r(document, "touchmove", c), r(document, "touchend", c), {
                name: "fastForward", get state() {
                    return (0, i.hasClass)(a, "art-fast-forward")
                }
            }
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}],
    "5dnKh": [function (e, t, r) {
        var a = e("@parcel/transformer-js/src/esmodule-helpers.js");
        a.defineInteropFlag(r), a.export(r, "default", () => o);
        var i = e("../utils");

        function o(e) {
            let {layers: t, icons: r, template: {$player: a}} = e;

            function o() {
                return (0, i.hasClass)(a, "art-lock")
            }

            function n() {
                (0, i.addClass)(a, "art-lock"), e.isLock = !0, e.emit("lock", !0)
            }

            function s() {
                (0, i.removeClass)(a, "art-lock"), e.isLock = !1, e.emit("lock", !1)
            }

            return t.add({
                name: "lock", mounted(t) {
                    let a = (0, i.append)(t, r.lock), o = (0, i.append)(t, r.unlock);
                    (0, i.setStyle)(a, "display", "none"), e.on("lock", e => {
                        e ? ((0, i.setStyle)(a, "display", "inline-flex"), (0, i.setStyle)(o, "display", "none")) : ((0, i.setStyle)(a, "display", "none"), (0, i.setStyle)(o, "display", "inline-flex"))
                    })
                }, click() {
                    o() ? s() : n()
                }
            }), {
                name: "lock", get state() {
                    return o()
                }, set state(value) {
                    value ? n() : s()
                }
            }
        }
    }, {"../utils": "h3rH9", "@parcel/transformer-js/src/esmodule-helpers.js": "guZOB"}]
}, ["abjMI"], "abjMI", "parcelRequireb749");
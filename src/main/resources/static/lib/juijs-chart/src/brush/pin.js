import jui from '../main.js';

export default {
    name: "chart.brush.pin",
    extend: "chart.brush.core",
    component: function () {
        const _ = jui.include("util.base");

        const PinBrush = function() {
            const self = this;

            this.draw = function() {
                const size = this.brush.size,
                    color = this.chart.theme("pinBorderColor"),
                    width = this.chart.theme("pinBorderWidth"),
                    fontSize = this.chart.theme("pinFontSize"),
                    paddingY = fontSize / 2,
                    startY = this.axis.area("y"),
                    showText = _.typeCheck("function", this.brush.format);

                return this.svg.group({}, function() {
                    const d = self.axis.x(self.brush.split),
                        x = d - (size / 2);

                    if(showText) {
                        const value = self.format(self.axis.x.invert(d));

                        self.chart.text({
                            "text-anchor": "middle",
                            "font-size": fontSize,
                            "fill": self.chart.theme("pinFontColor")
                        }, value).translate(d, startY);
                    }

                    self.svg.polygon({
                        fill: color
                    })
                        .point(size, startY)
                        .point(size / 2, size + startY)
                        .point(0, startY)
                        .translate(x, paddingY);

                    self.svg.line({
                        stroke: color,
                        "stroke-width": width,
                        x1: size / 2,
                        y1: startY + paddingY,
                        x2: size / 2,
                        y2: startY + self.axis.area("height")
                    }).translate(x, 0);
                });
            }
        }

        PinBrush.setup = function() {
            return {
                /** @cfg {Number} [size=6] */
                size: 6,
                /** @cfg {Number} [split=0] Determines a location where a pin is displayed (data index). */
                split: 0,
                /** @cfg {Function} [format=null] */
                format: null,
                /** @cfg {boolean} [clip=false] If the brush is drawn outside of the chart, cut the area. */
                clip : false
            };
        }

        return PinBrush;
    }
}

(ns fn-fx.controls
  (:refer-clojure :exclude [when ])
  (:require [fn-fx.render-core :as render-core] [fn-fx.diff :as diff]))

(set! clojure.core/*warn-on-reflection* true)

(defmacro when [& {:as props}] (fn-fx.render-core/value-type-impl javafx.beans.binding.When props))

(fn-fx.render-core/register-value-converter javafx.beans.binding.When)

(defmacro tree-item-property-value-factory [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.cell.TreeItemPropertyValueFactory props))

(fn-fx.render-core/register-value-converter javafx.scene.control.cell.TreeItemPropertyValueFactory)

(defmacro host-services [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.application.HostServices props))

(fn-fx.render-core/register-value-converter javafx.application.HostServices)

(defmacro paper [& {:as props}] (fn-fx.render-core/value-type-impl javafx.print.Paper props))

(fn-fx.render-core/register-value-converter javafx.print.Paper)

(defmacro local-date-time-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.LocalDateTimeStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.LocalDateTimeStringConverter)

(defmacro border [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.Border props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.Border)

(defmacro background-position [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BackgroundPosition props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BackgroundPosition)

(defmacro printer-attributes [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.print.PrinterAttributes props))

(fn-fx.render-core/register-value-converter javafx.print.PrinterAttributes)

(defmacro currency-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.CurrencyStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.CurrencyStringConverter)

(defmacro weak-event-handler [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.event.WeakEventHandler props))

(fn-fx.render-core/register-value-converter javafx.event.WeakEventHandler)

(defmacro property-value-factory [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.cell.PropertyValueFactory props))

(fn-fx.render-core/register-value-converter javafx.scene.control.cell.PropertyValueFactory)

(defmacro local-date-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.LocalDateStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.LocalDateStringConverter)

(defmacro corner-radii [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.CornerRadii props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.CornerRadii)

(defmacro index-range [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.IndexRange props))

(fn-fx.render-core/register-value-converter javafx.scene.control.IndexRange)

(defmacro byte-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.ByteStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.ByteStringConverter)

(defmacro video-track [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.media.VideoTrack props))

(fn-fx.render-core/register-value-converter javafx.scene.media.VideoTrack)

(defmacro bindings [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.beans.binding.Bindings props))

(fn-fx.render-core/register-value-converter javafx.beans.binding.Bindings)

(defmacro background-fill [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BackgroundFill props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BackgroundFill)

(defmacro java-f-x-builder-factory [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.fxml.JavaFXBuilderFactory props))

(fn-fx.render-core/register-value-converter javafx.fxml.JavaFXBuilderFactory)

(defmacro short-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.ShortStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.ShortStringConverter)

(defmacro weak-change-listener [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.beans.value.WeakChangeListener props))

(fn-fx.render-core/register-value-converter javafx.beans.value.WeakChangeListener)

(defmacro weak-set-change-listener [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.collections.WeakSetChangeListener props))

(fn-fx.render-core/register-value-converter javafx.collections.WeakSetChangeListener)

(defmacro styleable-property-factory [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.css.StyleablePropertyFactory props))

(fn-fx.render-core/register-value-converter javafx.css.StyleablePropertyFactory)

(defmacro popup-features [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.web.PopupFeatures props))

(fn-fx.render-core/register-value-converter javafx.scene.web.PopupFeatures)

(defmacro parsed-value [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.css.ParsedValue props))

(fn-fx.render-core/register-value-converter javafx.css.ParsedValue)

(defmacro swing-f-x-utils [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.embed.swing.SwingFXUtils props))

(fn-fx.render-core/register-value-converter javafx.embed.swing.SwingFXUtils)

(defmacro boolean-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.BooleanStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.BooleanStringConverter)

(defmacro touch-point [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.input.TouchPoint props))

(fn-fx.render-core/register-value-converter javafx.scene.input.TouchPoint)

(defmacro bounding-box [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.geometry.BoundingBox props))

(fn-fx.render-core/register-value-converter javafx.geometry.BoundingBox)

(defmacro page-range [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.print.PageRange props))

(fn-fx.render-core/register-value-converter javafx.print.PageRange)

(defmacro point2-d [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.geometry.Point2D props))

(fn-fx.render-core/register-value-converter javafx.geometry.Point2D)

(defmacro paper-source [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.print.PaperSource props))

(fn-fx.render-core/register-value-converter javafx.print.PaperSource)

(defmacro style-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.css.StyleConverter props))

(fn-fx.render-core/register-value-converter javafx.css.StyleConverter)

(defmacro audio-track [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.media.AudioTrack props))

(fn-fx.render-core/register-value-converter javafx.scene.media.AudioTrack)

(defmacro scene-antialiasing [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.SceneAntialiasing props))

(fn-fx.render-core/register-value-converter javafx.scene.SceneAntialiasing)

(defmacro input-method-text-run [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.input.InputMethodTextRun props))

(fn-fx.render-core/register-value-converter javafx.scene.input.InputMethodTextRun)

(defmacro dimension2-d [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.geometry.Dimension2D props))

(fn-fx.render-core/register-value-converter javafx.geometry.Dimension2D)

(defmacro double-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.DoubleStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.DoubleStringConverter)

(defmacro format-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.FormatStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.FormatStringConverter)

(defmacro integer-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.IntegerStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.IntegerStringConverter)

(defmacro pick-result [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.input.PickResult props))

(fn-fx.render-core/register-value-converter javafx.scene.input.PickResult)

(defmacro data-format [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.input.DataFormat props))

(fn-fx.render-core/register-value-converter javafx.scene.input.DataFormat)

(defmacro border-widths [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BorderWidths props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BorderWidths)

(defmacro background [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.Background props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.Background)

(defmacro key-frame [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.animation.KeyFrame props))

(fn-fx.render-core/register-value-converter javafx.animation.KeyFrame)

(defmacro insets [& {:as props}] (fn-fx.render-core/value-type-impl javafx.geometry.Insets props))

(fn-fx.render-core/register-value-converter javafx.geometry.Insets)

(defmacro image-cursor [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.ImageCursor props))

(fn-fx.render-core/register-value-converter javafx.scene.ImageCursor)

(defmacro big-integer-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.BigIntegerStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.BigIntegerStringConverter)

(defmacro weak-map-change-listener [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.collections.WeakMapChangeListener props))

(fn-fx.render-core/register-value-converter javafx.collections.WeakMapChangeListener)

(defmacro f-x-collections [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.collections.FXCollections props))

(fn-fx.render-core/register-value-converter javafx.collections.FXCollections)

(defmacro screen [& {:as props}] (fn-fx.render-core/value-type-impl javafx.stage.Screen props))

(fn-fx.render-core/register-value-converter javafx.stage.Screen)

(defmacro time-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.TimeStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.TimeStringConverter)

(defmacro font [& {:as props}] (fn-fx.render-core/value-type-impl javafx.scene.text.Font props))

(fn-fx.render-core/register-value-converter javafx.scene.text.Font)

(defmacro radial-gradient [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.paint.RadialGradient props))

(fn-fx.render-core/register-value-converter javafx.scene.paint.RadialGradient)

(defmacro border-stroke [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BorderStroke props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BorderStroke)

(defmacro big-decimal-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.BigDecimalStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.BigDecimalStringConverter)

(defmacro prompt-data [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.web.PromptData props))

(fn-fx.render-core/register-value-converter javafx.scene.web.PromptData)

(defmacro subtitle-track [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.media.SubtitleTrack props))

(fn-fx.render-core/register-value-converter javafx.scene.media.SubtitleTrack)

(defmacro key-character-combination [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.input.KeyCharacterCombination props))

(fn-fx.render-core/register-value-converter javafx.scene.input.KeyCharacterCombination)

(defmacro date-time-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.DateTimeStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.DateTimeStringConverter)

(defmacro border-stroke-style [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BorderStrokeStyle props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BorderStrokeStyle)

(defmacro key-code-combination [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.input.KeyCodeCombination props))

(fn-fx.render-core/register-value-converter javafx.scene.input.KeyCodeCombination)

(defmacro button-type [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.ButtonType props))

(fn-fx.render-core/register-value-converter javafx.scene.control.ButtonType)

(defmacro local-time-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.LocalTimeStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.LocalTimeStringConverter)

(defmacro background-size [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BackgroundSize props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BackgroundSize)

(defmacro rectangle2-d [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.geometry.Rectangle2D props))

(fn-fx.render-core/register-value-converter javafx.geometry.Rectangle2D)

(defmacro vertex-format [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.shape.VertexFormat props))

(fn-fx.render-core/register-value-converter javafx.scene.shape.VertexFormat)

(defmacro background-image [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BackgroundImage props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BackgroundImage)

(defmacro map-value-factory [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.cell.MapValueFactory props))

(fn-fx.render-core/register-value-converter javafx.scene.control.cell.MapValueFactory)

(defmacro snapshot-result [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.SnapshotResult props))

(fn-fx.render-core/register-value-converter javafx.scene.SnapshotResult)

(defmacro stop [& {:as props}] (fn-fx.render-core/value-type-impl javafx.scene.paint.Stop props))

(fn-fx.render-core/register-value-converter javafx.scene.paint.Stop)

(defmacro float-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.FloatStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.FloatStringConverter)

(defmacro printer [& {:as props}] (fn-fx.render-core/value-type-impl javafx.print.Printer props))

(fn-fx.render-core/register-value-converter javafx.print.Printer)

(defmacro event-type [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.event.EventType props))

(fn-fx.render-core/register-value-converter javafx.event.EventType)

(defmacro date-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.DateStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.DateStringConverter)

(defmacro point3-d [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.geometry.Point3D props))

(fn-fx.render-core/register-value-converter javafx.geometry.Point3D)

(defmacro tree-table-position [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.TreeTablePosition props))

(fn-fx.render-core/register-value-converter javafx.scene.control.TreeTablePosition)

(defmacro weak-list-change-listener [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.collections.WeakListChangeListener props))

(fn-fx.render-core/register-value-converter javafx.collections.WeakListChangeListener)

(defmacro number-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.NumberStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.NumberStringConverter)

(defmacro image-pattern [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.paint.ImagePattern props))

(fn-fx.render-core/register-value-converter javafx.scene.paint.ImagePattern)

(defmacro writable-image [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.image.WritableImage props))

(fn-fx.render-core/register-value-converter javafx.scene.image.WritableImage)

(defmacro character-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.CharacterStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.CharacterStringConverter)

(defmacro long-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.LongStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.LongStringConverter)

(defmacro linear-gradient [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.paint.LinearGradient props))

(fn-fx.render-core/register-value-converter javafx.scene.paint.LinearGradient)

(defmacro image [& {:as props}] (fn-fx.render-core/value-type-impl javafx.scene.image.Image props))

(fn-fx.render-core/register-value-converter javafx.scene.image.Image)

(defmacro key-value [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.animation.KeyValue props))

(fn-fx.render-core/register-value-converter javafx.animation.KeyValue)

(defmacro border-image [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BorderImage props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BorderImage)

(defmacro page-layout [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.print.PageLayout props))

(fn-fx.render-core/register-value-converter javafx.print.PageLayout)

(defmacro weak-invalidation-listener [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.beans.WeakInvalidationListener props))

(fn-fx.render-core/register-value-converter javafx.beans.WeakInvalidationListener)

(defmacro color [& {:as props}] (fn-fx.render-core/value-type-impl javafx.scene.paint.Color props))

(fn-fx.render-core/register-value-converter javafx.scene.paint.Color)

(defmacro print-resolution [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.print.PrintResolution props))

(fn-fx.render-core/register-value-converter javafx.print.PrintResolution)

(defmacro table-position [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.TablePosition props))

(fn-fx.render-core/register-value-converter javafx.scene.control.TablePosition)

(defmacro default-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.DefaultStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.DefaultStringConverter)

(defmacro pair [& {:as props}] (fn-fx.render-core/value-type-impl javafx.util.Pair props))

(fn-fx.render-core/register-value-converter javafx.util.Pair)

(defmacro duration [& {:as props}] (fn-fx.render-core/value-type-impl javafx.util.Duration props))

(fn-fx.render-core/register-value-converter javafx.util.Duration)

(defmacro percentage-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.PercentageStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.PercentageStringConverter)

(defmacro stacked-bar-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.StackedBarChart props #{}))

(defmacro text-flow [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.text.TextFlow props #{}))

(defmacro alert [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Alert props #{}))

(defmacro shadow [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.Shadow props #{}))

(defmacro button-bar [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ButtonBar props #{}))

(defmacro motion-blur [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.MotionBlur props #{}))

(defmacro split-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.SplitPane props #{}))

(defmacro box [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.shape.Box props #{}))

(defmacro combo-box-tree-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ComboBoxTreeTableCell props #{}))

(defmacro drop-shadow [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.DropShadow props #{}))

(defmacro custom-menu-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.CustomMenuItem props #{:node}))

(defmacro separator [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Separator props #{}))

(defmacro audio-clip [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.media.AudioClip props #{:source}))

(defmacro lighting [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.Lighting props #{}))

(defmacro category-axis [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.CategoryAxis props #{}))

(defmacro text-input-dialog [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TextInputDialog props #{:default-value}))

(defmacro text-formatter [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TextFormatter props #{}))

(defmacro read-only-list-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl
    :javafx.beans.property.ReadOnlyListWrapper
    props
    #{:bean :name}))

(defmacro stroke-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.StrokeTransition props #{}))

(defmacro date-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.DateCell props #{}))

(defmacro translate-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.TranslateTransition props #{}))

(defmacro date-picker [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.DatePicker props #{:local-date}))

(defmacro tree-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeTableCell props #{}))

(defmacro tab-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TabPane props #{}))

(defmacro cubic-curve [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.CubicCurve props #{}))

(defmacro media-player [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.media.MediaPlayer props #{:media}))

(defmacro parallel-camera [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.ParallelCamera props #{}))

(defmacro text-field-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.TextFieldTableCell props #{}))

(defmacro text-field-tree-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.TextFieldTreeTableCell props #{}))

(defmacro button [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Button props #{}))

(defmacro flow-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.FlowPane props #{}))

(defmacro check-box-tree-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.CheckBoxTreeTableCell props #{}))

(defmacro stage [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.stage.Stage props #{:style}))

(defmacro list-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ListCell props #{}))

(defmacro progress-bar [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ProgressBar props #{}))

(defmacro media [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.media.Media props #{:source}))

(defmacro glow [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.Glow props #{}))

(defmacro tab [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Tab props #{}))

(defmacro choice-box-list-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ChoiceBoxListCell props #{}))

(defmacro tree-table-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeTableView props #{}))

(defmacro split-menu-button [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.SplitMenuButton props #{}))

(defmacro separator-menu-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.SeparatorMenuItem props #{}))

(defmacro color-adjust [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.ColorAdjust props #{}))

(defmacro titled-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TitledPane props #{:title}))

(defmacro text [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.text.Text props #{}))

(defmacro v-line-to [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.VLineTo props #{}))

(defmacro scene [& {:as props}]
  (fn-fx.render-core/component-impl
    :javafx.scene.Scene
    props
    #{:depth-buffer :width :anti-aliasing :height}))

(defmacro color-picker [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ColorPicker props #{:color}))

(defmacro dialog [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Dialog props #{}))

(defmacro quad-curve-to [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.QuadCurveTo props #{}))

(defmacro menu-button [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.MenuButton props #{}))

(defmacro triangle-mesh [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.TriangleMesh props #{}))

(defmacro tooltip [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Tooltip props #{}))

(defmacro path [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Path props #{}))

(defmacro move-to [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.MoveTo props #{}))

(defmacro scroll-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ScrollPane props #{}))

(defmacro bloom [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.Bloom props #{}))

(defmacro progress-indicator [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ProgressIndicator props #{}))

(defmacro tool-bar [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ToolBar props #{}))

(defmacro read-only-map-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.beans.property.ReadOnlyMapWrapper props #{:bean :name}))

(defmacro sepia-tone [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.SepiaTone props #{}))

(defmacro sequential-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.SequentialTransition props #{}))

(defmacro phong-material [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.paint.PhongMaterial props #{}))

(defmacro read-only-boolean-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl
    :javafx.beans.property.ReadOnlyBooleanWrapper
    props
    #{:bean :name :initial-value}))

(defmacro hyperlink [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Hyperlink props #{}))

(defmacro fade-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.FadeTransition props #{}))

(defmacro bar-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.BarChart props #{}))

(defmacro scale [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.transform.Scale props #{}))

(defmacro accordion [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Accordion props #{}))

(defmacro grid-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.GridPane props #{}))

(defmacro image-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.image.ImageView props #{:url}))

(defmacro point-light [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.PointLight props #{}))

(defmacro inner-shadow [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.InnerShadow props #{}))

(defmacro column-constraints [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.ColumnConstraints props #{:width}))

(defmacro check-box [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.CheckBox props #{}))

(defmacro combo-box-tree-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ComboBoxTreeCell props #{}))

(defmacro float-map [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.FloatMap props #{}))

(defmacro timeline [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.Timeline props #{:target-framerate}))

(defmacro table-row [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TableRow props #{}))

(defmacro equalizer-band [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.media.EqualizerBand props #{}))

(defmacro context-menu [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ContextMenu props #{}))

(defmacro rotate-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.RotateTransition props #{}))

(defmacro web-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.web.WebView props #{}))

(defmacro parallel-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.ParallelTransition props #{}))

(defmacro area-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.AreaChart props #{}))

(defmacro password-field [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.PasswordField props #{}))

(defmacro line [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Line props #{}))

(defmacro popup [& {:as props}] (fn-fx.render-core/component-impl :javafx.stage.Popup props #{}))

(defmacro f-x-m-l-loader [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.fxml.FXMLLoader props #{}))

(defmacro combo-box-list-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ComboBoxListCell props #{}))

(defmacro perspective-camera [& {:as props}]
  (fn-fx.render-core/component-impl
    :javafx.scene.PerspectiveCamera
    props
    #{:fixed-eye-at-camera-zero}))

(defmacro rectangle [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Rectangle props #{}))

(defmacro read-only-float-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl
    :javafx.beans.property.ReadOnlyFloatWrapper
    props
    #{:bean :name :initial-value}))

(defmacro perspective-transform [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.PerspectiveTransform props #{}))

(defmacro h-line-to [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.HLineTo props #{}))

(defmacro box-blur [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.BoxBlur props #{}))

(defmacro check-box-tree-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.CheckBoxTreeCell props #{}))

(defmacro scatter-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.ScatterChart props #{}))

(defmacro slider [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Slider props #{}))

(defmacro menu-bar [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.MenuBar props #{}))

(defmacro web-engine [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.web.WebEngine props #{:url}))

(defmacro indexed-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.IndexedCell props #{}))

(defmacro toggle-group [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ToggleGroup props #{}))

(defmacro sphere [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Sphere props #{:divisions}))

(defmacro sorted-list [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.collections.transformation.SortedList props #{}))

(defmacro translate [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.transform.Translate props #{}))

(defmacro mnemonic [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.input.Mnemonic props #{}))

(defmacro text-area [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TextArea props #{}))

(defmacro toggle-button [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ToggleButton props #{}))

(defmacro file-chooser [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.stage.FileChooser props #{}))

(defmacro filtered-list [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.collections.transformation.FilteredList props #{}))

(defmacro tree-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeView props #{}))

(defmacro swing-node [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.embed.swing.SwingNode props #{}))

(defmacro progress-bar-tree-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ProgressBarTreeTableCell props #{}))

(defmacro check-box-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.CheckBoxTableCell props #{}))

(defmacro read-only-double-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl
    :javafx.beans.property.ReadOnlyDoubleWrapper
    props
    #{:bean :name :initial-value}))

(defmacro s-v-g-path [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.SVGPath props #{}))

(defmacro fill-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.FillTransition props #{}))

(defmacro pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.Pane props #{}))

(defmacro rotate [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.transform.Rotate props #{}))

(defmacro table-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TableView props #{}))

(defmacro dialog-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.DialogPane props #{}))

(defmacro pagination [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Pagination props #{:page-index}))

(defmacro table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TableCell props #{}))

(defmacro color-input [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.ColorInput props #{}))

(defmacro pause-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.PauseTransition props #{}))

(defmacro tree-table-row [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeTableRow props #{}))

(defmacro menu [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Menu props #{}))

(defmacro text-field-list-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.TextFieldListCell props #{}))

(defmacro read-only-object-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl
    :javafx.beans.property.ReadOnlyObjectWrapper
    props
    #{:bean :name :initial-value}))

(defmacro v-box [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.VBox props #{}))

(defmacro check-box-tree-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.CheckBoxTreeItem props #{}))

(defmacro menu-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.MenuItem props #{}))

(defmacro directory-chooser [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.stage.DirectoryChooser props #{}))

(defmacro line-to [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.LineTo props #{}))

(defmacro read-only-long-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl
    :javafx.beans.property.ReadOnlyLongWrapper
    props
    #{:bean :name :initial-value}))

(defmacro popup-control [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.PopupControl props #{}))

(defmacro text-field [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TextField props #{}))

(defmacro radio-button [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.RadioButton props #{}))

(defmacro polyline [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Polyline props #{}))

(defmacro sub-scene [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.SubScene props #{:depth-buffer :anti-aliasing}))

(defmacro combo-box [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ComboBox props #{}))

(defmacro h-t-m-l-editor [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.web.HTMLEditor props #{}))

(defmacro radio-menu-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.RadioMenuItem props #{}))

(defmacro polygon [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Polygon props #{}))

(defmacro mesh-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.MeshView props #{}))

(defmacro circle [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Circle props #{}))

(defmacro cubic-curve-to [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.CubicCurveTo props #{}))

(defmacro choice-box-tree-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ChoiceBoxTreeTableCell props #{}))

(defmacro choice-box-tree-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ChoiceBoxTreeCell props #{}))

(defmacro tree-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeItem props #{}))

(defmacro text-field-tree-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.TextFieldTreeCell props #{}))

(defmacro choice-box-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ChoiceBoxTableCell props #{}))

(defmacro quad-curve [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.QuadCurve props #{}))

(defmacro affine [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.transform.Affine props #{:transform}))

(defmacro ellipse [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Ellipse props #{}))

(defmacro row-constraints [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.RowConstraints props #{:height}))

(defmacro read-only-string-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl
    :javafx.beans.property.ReadOnlyStringWrapper
    props
    #{:bean :name :initial-value}))

(defmacro border-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.BorderPane props #{}))

(defmacro displacement-map [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.DisplacementMap props #{}))

(defmacro check-box-list-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.CheckBoxListCell props #{}))

(defmacro spinner [& {:as props}]
  (fn-fx.render-core/component-impl
    :javafx.scene.control.Spinner
    props
    #{:amount-to-step-by :min :max :initial-value}))

(defmacro group [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.Group props #{}))

(defmacro tree-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeCell props #{}))

(defmacro pie-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.PieChart props #{}))

(defmacro path-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.PathTransition props #{}))

(defmacro media-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.media.MediaView props #{}))

(defmacro tree-table-column [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeTableColumn props #{}))

(defmacro check-menu-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.CheckMenuItem props #{}))

(defmacro table-column [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TableColumn props #{}))

(defmacro progress-bar-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ProgressBarTableCell props #{}))

(defmacro blend [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.Blend props #{}))

(defmacro label [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Label props #{}))

(defmacro gaussian-blur [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.GaussianBlur props #{}))

(defmacro line-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.LineChart props #{}))

(defmacro choice-box [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ChoiceBox props #{}))

(defmacro anchor-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.AnchorPane props #{}))

(defmacro scroll-bar [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ScrollBar props #{}))

(defmacro scale-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.ScaleTransition props #{}))

(defmacro cylinder [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Cylinder props #{:divisions}))

(defmacro canvas [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.canvas.Canvas props #{}))

(defmacro choice-dialog [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ChoiceDialog props #{}))

(defmacro read-only-set-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.beans.property.ReadOnlySetWrapper props #{:bean :name}))

(defmacro reflection [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.Reflection props #{}))

(defmacro image-input [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.ImageInput props #{}))

(defmacro ambient-light [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.AmbientLight props #{}))

(defmacro region [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.Region props #{}))

(defmacro cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Cell props #{}))

(defmacro list-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ListView props #{}))

(defmacro arc-to [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.ArcTo props #{}))

(defmacro arc [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.shape.Arc props #{}))

(defmacro shear [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.transform.Shear props #{}))

(defmacro number-axis [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.NumberAxis props #{:axis-label}))

(defmacro combo-box-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ComboBoxTableCell props #{}))

(defmacro tile-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.TilePane props #{}))

(defmacro close-path [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.ClosePath props #{}))

(defmacro bubble-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.BubbleChart props #{}))

(defmacro read-only-integer-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl
    :javafx.beans.property.ReadOnlyIntegerWrapper
    props
    #{:bean :name :initial-value}))

(defmacro h-box [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.HBox props #{}))

(defmacro stacked-area-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.StackedAreaChart props #{}))

(defmacro snapshot-parameters [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.SnapshotParameters props #{}))

(defmacro stack-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.StackPane props #{}))

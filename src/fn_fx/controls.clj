
(ns fn-fx.controls
  (:refer-clojure :exclude [when ])
  (:require [fn-fx.render-core :as render-core] [fn-fx.diff :as diff]))

(set! clojure.core/*warn-on-reflection* true)

(defmacro character-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.CharacterStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.CharacterStringConverter)

(defmacro scene-antialiasing [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.SceneAntialiasing props))

(fn-fx.render-core/register-value-converter javafx.scene.SceneAntialiasing)

(defmacro duration [& {:as props}] (fn-fx.render-core/value-type-impl javafx.util.Duration props))

(fn-fx.render-core/register-value-converter javafx.util.Duration)

(defmacro event-type [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.event.EventType props))

(fn-fx.render-core/register-value-converter javafx.event.EventType)

(defmacro key-code-combination [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.input.KeyCodeCombination props))

(fn-fx.render-core/register-value-converter javafx.scene.input.KeyCodeCombination)

(defmacro font [& {:as props}] (fn-fx.render-core/value-type-impl javafx.scene.text.Font props))

(fn-fx.render-core/register-value-converter javafx.scene.text.Font)

(defmacro image [& {:as props}] (fn-fx.render-core/value-type-impl javafx.scene.image.Image props))

(fn-fx.render-core/register-value-converter javafx.scene.image.Image)

(defmacro java-f-x-builder-factory [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.fxml.JavaFXBuilderFactory props))

(fn-fx.render-core/register-value-converter javafx.fxml.JavaFXBuilderFactory)

(defmacro host-services [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.application.HostServices props))

(fn-fx.render-core/register-value-converter javafx.application.HostServices)

(defmacro key-frame [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.animation.KeyFrame props))

(fn-fx.render-core/register-value-converter javafx.animation.KeyFrame)

(defmacro number-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.NumberStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.NumberStringConverter)

(defmacro local-date-time-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.LocalDateTimeStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.LocalDateTimeStringConverter)

(defmacro border-image [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BorderImage props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BorderImage)

(defmacro print-resolution [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.print.PrintResolution props))

(fn-fx.render-core/register-value-converter javafx.print.PrintResolution)

(defmacro key-character-combination [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.input.KeyCharacterCombination props))

(fn-fx.render-core/register-value-converter javafx.scene.input.KeyCharacterCombination)

(defmacro format-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.FormatStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.FormatStringConverter)

(defmacro weak-event-handler [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.event.WeakEventHandler props))

(fn-fx.render-core/register-value-converter javafx.event.WeakEventHandler)

(defmacro data-format [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.input.DataFormat props))

(fn-fx.render-core/register-value-converter javafx.scene.input.DataFormat)

(defmacro popup-features [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.web.PopupFeatures props))

(fn-fx.render-core/register-value-converter javafx.scene.web.PopupFeatures)

(defmacro screen [& {:as props}] (fn-fx.render-core/value-type-impl javafx.stage.Screen props))

(fn-fx.render-core/register-value-converter javafx.stage.Screen)

(defmacro rectangle2-d [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.geometry.Rectangle2D props))

(fn-fx.render-core/register-value-converter javafx.geometry.Rectangle2D)

(defmacro weak-invalidation-listener [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.beans.WeakInvalidationListener props))

(fn-fx.render-core/register-value-converter javafx.beans.WeakInvalidationListener)

(defmacro pick-result [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.input.PickResult props))

(fn-fx.render-core/register-value-converter javafx.scene.input.PickResult)

(defmacro vertex-format [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.shape.VertexFormat props))

(fn-fx.render-core/register-value-converter javafx.scene.shape.VertexFormat)

(defmacro local-time-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.LocalTimeStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.LocalTimeStringConverter)

(defmacro subtitle-track [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.media.SubtitleTrack props))

(fn-fx.render-core/register-value-converter javafx.scene.media.SubtitleTrack)

(defmacro local-date-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.LocalDateStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.LocalDateStringConverter)

(defmacro weak-change-listener [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.beans.value.WeakChangeListener props))

(fn-fx.render-core/register-value-converter javafx.beans.value.WeakChangeListener)

(defmacro index-range [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.IndexRange props))

(fn-fx.render-core/register-value-converter javafx.scene.control.IndexRange)

(defmacro border [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.Border props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.Border)

(defmacro weak-list-change-listener [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.collections.WeakListChangeListener props))

(fn-fx.render-core/register-value-converter javafx.collections.WeakListChangeListener)

(defmacro byte-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.ByteStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.ByteStringConverter)

(defmacro bounding-box [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.geometry.BoundingBox props))

(fn-fx.render-core/register-value-converter javafx.geometry.BoundingBox)

(defmacro insets [& {:as props}] (fn-fx.render-core/value-type-impl javafx.geometry.Insets props))

(fn-fx.render-core/register-value-converter javafx.geometry.Insets)

(defmacro border-stroke [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BorderStroke props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BorderStroke)

(defmacro snapshot-result [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.SnapshotResult props))

(fn-fx.render-core/register-value-converter javafx.scene.SnapshotResult)

(defmacro integer-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.IntegerStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.IntegerStringConverter)

(defmacro image-cursor [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.ImageCursor props))

(fn-fx.render-core/register-value-converter javafx.scene.ImageCursor)

(defmacro percentage-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.PercentageStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.PercentageStringConverter)

(defmacro date-time-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.DateTimeStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.DateTimeStringConverter)

(defmacro pair [& {:as props}] (fn-fx.render-core/value-type-impl javafx.util.Pair props))

(fn-fx.render-core/register-value-converter javafx.util.Pair)

(defmacro currency-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.CurrencyStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.CurrencyStringConverter)

(defmacro paper-source [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.print.PaperSource props))

(fn-fx.render-core/register-value-converter javafx.print.PaperSource)

(defmacro default-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.DefaultStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.DefaultStringConverter)

(defmacro background-fill [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BackgroundFill props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BackgroundFill)

(defmacro linear-gradient [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.paint.LinearGradient props))

(fn-fx.render-core/register-value-converter javafx.scene.paint.LinearGradient)

(defmacro short-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.ShortStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.ShortStringConverter)

(defmacro f-x-collections [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.collections.FXCollections props))

(fn-fx.render-core/register-value-converter javafx.collections.FXCollections)

(defmacro corner-radii [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.CornerRadii props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.CornerRadii)

(defmacro weak-set-change-listener [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.collections.WeakSetChangeListener props))

(fn-fx.render-core/register-value-converter javafx.collections.WeakSetChangeListener)

(defmacro radial-gradient [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.paint.RadialGradient props))

(fn-fx.render-core/register-value-converter javafx.scene.paint.RadialGradient)

(defmacro property-value-factory [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.cell.PropertyValueFactory props))

(fn-fx.render-core/register-value-converter javafx.scene.control.cell.PropertyValueFactory)

(defmacro writable-image [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.image.WritableImage props))

(fn-fx.render-core/register-value-converter javafx.scene.image.WritableImage)

(defmacro long-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.LongStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.LongStringConverter)

(defmacro image-pattern [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.paint.ImagePattern props))

(fn-fx.render-core/register-value-converter javafx.scene.paint.ImagePattern)

(defmacro button-type [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.ButtonType props))

(fn-fx.render-core/register-value-converter javafx.scene.control.ButtonType)

(defmacro touch-point [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.input.TouchPoint props))

(fn-fx.render-core/register-value-converter javafx.scene.input.TouchPoint)

(defmacro printer-attributes [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.print.PrinterAttributes props))

(fn-fx.render-core/register-value-converter javafx.print.PrinterAttributes)

(defmacro color [& {:as props}] (fn-fx.render-core/value-type-impl javafx.scene.paint.Color props))

(fn-fx.render-core/register-value-converter javafx.scene.paint.Color)

(defmacro audio-track [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.media.AudioTrack props))

(fn-fx.render-core/register-value-converter javafx.scene.media.AudioTrack)

(defmacro paper [& {:as props}] (fn-fx.render-core/value-type-impl javafx.print.Paper props))

(fn-fx.render-core/register-value-converter javafx.print.Paper)

(defmacro point3-d [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.geometry.Point3D props))

(fn-fx.render-core/register-value-converter javafx.geometry.Point3D)

(defmacro when [& {:as props}] (fn-fx.render-core/value-type-impl javafx.beans.binding.When props))

(fn-fx.render-core/register-value-converter javafx.beans.binding.When)

(defmacro video-track [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.media.VideoTrack props))

(fn-fx.render-core/register-value-converter javafx.scene.media.VideoTrack)

(defmacro bindings [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.beans.binding.Bindings props))

(fn-fx.render-core/register-value-converter javafx.beans.binding.Bindings)

(defmacro date-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.DateStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.DateStringConverter)

(defmacro swing-f-x-utils [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.embed.swing.SwingFXUtils props))

(fn-fx.render-core/register-value-converter javafx.embed.swing.SwingFXUtils)

(defmacro key-value [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.animation.KeyValue props))

(fn-fx.render-core/register-value-converter javafx.animation.KeyValue)

(defmacro tree-table-position [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.TreeTablePosition props))

(fn-fx.render-core/register-value-converter javafx.scene.control.TreeTablePosition)

(defmacro map-value-factory [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.cell.MapValueFactory props))

(fn-fx.render-core/register-value-converter javafx.scene.control.cell.MapValueFactory)

(defmacro table-position [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.TablePosition props))

(fn-fx.render-core/register-value-converter javafx.scene.control.TablePosition)

(defmacro styleable-property-factory [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.css.StyleablePropertyFactory props))

(fn-fx.render-core/register-value-converter javafx.css.StyleablePropertyFactory)

(defmacro tree-item-property-value-factory [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.control.cell.TreeItemPropertyValueFactory props))

(fn-fx.render-core/register-value-converter javafx.scene.control.cell.TreeItemPropertyValueFactory)

(defmacro background-image [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BackgroundImage props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BackgroundImage)

(defmacro parsed-value [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.css.ParsedValue props))

(fn-fx.render-core/register-value-converter javafx.css.ParsedValue)

(defmacro border-widths [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BorderWidths props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BorderWidths)

(defmacro big-decimal-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.BigDecimalStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.BigDecimalStringConverter)

(defmacro point2-d [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.geometry.Point2D props))

(fn-fx.render-core/register-value-converter javafx.geometry.Point2D)

(defmacro background-position [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BackgroundPosition props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BackgroundPosition)

(defmacro stop [& {:as props}] (fn-fx.render-core/value-type-impl javafx.scene.paint.Stop props))

(fn-fx.render-core/register-value-converter javafx.scene.paint.Stop)

(defmacro boolean-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.BooleanStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.BooleanStringConverter)

(defmacro double-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.DoubleStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.DoubleStringConverter)

(defmacro weak-map-change-listener [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.collections.WeakMapChangeListener props))

(fn-fx.render-core/register-value-converter javafx.collections.WeakMapChangeListener)

(defmacro float-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.FloatStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.FloatStringConverter)

(defmacro page-range [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.print.PageRange props))

(fn-fx.render-core/register-value-converter javafx.print.PageRange)

(defmacro input-method-text-run [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.input.InputMethodTextRun props))

(fn-fx.render-core/register-value-converter javafx.scene.input.InputMethodTextRun)

(defmacro background-size [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BackgroundSize props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BackgroundSize)

(defmacro printer [& {:as props}] (fn-fx.render-core/value-type-impl javafx.print.Printer props))

(fn-fx.render-core/register-value-converter javafx.print.Printer)

(defmacro background [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.Background props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.Background)

(defmacro time-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.TimeStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.TimeStringConverter)

(defmacro page-layout [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.print.PageLayout props))

(fn-fx.render-core/register-value-converter javafx.print.PageLayout)

(defmacro prompt-data [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.web.PromptData props))

(fn-fx.render-core/register-value-converter javafx.scene.web.PromptData)

(defmacro big-integer-string-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.util.converter.BigIntegerStringConverter props))

(fn-fx.render-core/register-value-converter javafx.util.converter.BigIntegerStringConverter)

(defmacro style-converter [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.css.StyleConverter props))

(fn-fx.render-core/register-value-converter javafx.css.StyleConverter)

(defmacro border-stroke-style [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.scene.layout.BorderStrokeStyle props))

(fn-fx.render-core/register-value-converter javafx.scene.layout.BorderStrokeStyle)

(defmacro dimension2-d [& {:as props}]
  (fn-fx.render-core/value-type-impl javafx.geometry.Dimension2D props))

(fn-fx.render-core/register-value-converter javafx.geometry.Dimension2D)

(defmacro button-bar [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ButtonBar props))

(defmacro h-box [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.layout.HBox props))

(defmacro rotate-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.RotateTransition props))

(defmacro f-x-m-l-loader [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.fxml.FXMLLoader props))

(defmacro grid-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.GridPane props))

(defmacro cylinder [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Cylinder props))

(defmacro cubic-curve [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.CubicCurve props))

(defmacro timeline [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.Timeline props))

(defmacro pause-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.PauseTransition props))

(defmacro affine [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.transform.Affine props))

(defmacro path-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.PathTransition props))

(defmacro label [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Label props))

(defmacro h-t-m-l-editor [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.web.HTMLEditor props))

(defmacro phong-material [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.paint.PhongMaterial props))

(defmacro color-input [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.ColorInput props))

(defmacro circle [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Circle props))

(defmacro combo-box-list-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ComboBoxListCell props))

(defmacro sequential-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.SequentialTransition props))

(defmacro scale-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.ScaleTransition props))

(defmacro web-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.web.WebView props))

(defmacro cubic-curve-to [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.CubicCurveTo props))

(defmacro menu [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.control.Menu props))

(defmacro scroll-bar [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ScrollBar props))

(defmacro check-menu-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.CheckMenuItem props))

(defmacro scroll-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ScrollPane props))

(defmacro file-chooser [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.stage.FileChooser props))

(defmacro gaussian-blur [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.GaussianBlur props))

(defmacro anchor-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.AnchorPane props))

(defmacro text-formatter [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TextFormatter props))

(defmacro read-only-double-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.beans.property.ReadOnlyDoubleWrapper props))

(defmacro custom-menu-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.CustomMenuItem props))

(defmacro reflection [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.Reflection props))

(defmacro combo-box [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ComboBox props))

(defmacro perspective-camera [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.PerspectiveCamera props))

(defmacro check-box-list-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.CheckBoxListCell props))

(defmacro stack-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.StackPane props))

(defmacro tree-table-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeTableView props))

(defmacro tooltip [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Tooltip props))

(defmacro s-v-g-path [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.SVGPath props))

(defmacro row-constraints [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.RowConstraints props))

(defmacro tab [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.control.Tab props))

(defmacro rotate [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.transform.Rotate props))

(defmacro triangle-mesh [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.TriangleMesh props))

(defmacro path [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.shape.Path props))

(defmacro check-box-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.CheckBoxTableCell props))

(defmacro v-box [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.layout.VBox props))

(defmacro choice-box [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ChoiceBox props))

(defmacro line-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.LineChart props))

(defmacro choice-box-tree-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ChoiceBoxTreeCell props))

(defmacro h-line-to [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.HLineTo props))

(defmacro read-only-map-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.beans.property.ReadOnlyMapWrapper props))

(defmacro number-axis [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.NumberAxis props))

(defmacro radio-menu-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.RadioMenuItem props))

(defmacro read-only-string-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.beans.property.ReadOnlyStringWrapper props))

(defmacro radio-button [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.RadioButton props))

(defmacro directory-chooser [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.stage.DirectoryChooser props))

(defmacro titled-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TitledPane props))

(defmacro choice-dialog [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ChoiceDialog props))

(defmacro toggle-button [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ToggleButton props))

(defmacro scatter-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.ScatterChart props))

(defmacro move-to [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.MoveTo props))

(defmacro stacked-bar-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.StackedBarChart props))

(defmacro combo-box-tree-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ComboBoxTreeTableCell props))

(defmacro box [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.shape.Box props))

(defmacro alert [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Alert props))

(defmacro popup-control [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.PopupControl props))

(defmacro mnemonic [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.input.Mnemonic props))

(defmacro audio-clip [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.media.AudioClip props))

(defmacro choice-box-tree-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ChoiceBoxTreeTableCell props))

(defmacro bubble-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.BubbleChart props))

(defmacro read-only-list-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.beans.property.ReadOnlyListWrapper props))

(defmacro snapshot-parameters [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.SnapshotParameters props))

(defmacro date-picker [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.DatePicker props))

(defmacro shadow [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.Shadow props))

(defmacro date-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.DateCell props))

(defmacro translate [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.transform.Translate props))

(defmacro tree-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeView props))

(defmacro media [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.media.Media props))

(defmacro area-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.AreaChart props))

(defmacro drop-shadow [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.DropShadow props))

(defmacro category-axis [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.CategoryAxis props))

(defmacro menu-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.MenuItem props))

(defmacro border-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.BorderPane props))

(defmacro sepia-tone [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.SepiaTone props))

(defmacro filtered-list [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.collections.transformation.FilteredList props))

(defmacro ellipse [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Ellipse props))

(defmacro line [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.shape.Line props))

(defmacro text-field-list-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.TextFieldListCell props))

(defmacro sphere [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Sphere props))

(defmacro read-only-set-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.beans.property.ReadOnlySetWrapper props))

(defmacro lighting [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.Lighting props))

(defmacro text-field-tree-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.TextFieldTreeTableCell props))

(defmacro dialog-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.DialogPane props))

(defmacro read-only-long-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.beans.property.ReadOnlyLongWrapper props))

(defmacro tab-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TabPane props))

(defmacro combo-box-tree-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ComboBoxTreeCell props))

(defmacro shear [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.transform.Shear props))

(defmacro list-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ListCell props))

(defmacro polyline [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Polyline props))

(defmacro arc-to [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.shape.ArcTo props))

(defmacro equalizer-band [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.media.EqualizerBand props))

(defmacro polygon [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Polygon props))

(defmacro media-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.media.MediaView props))

(defmacro swing-node [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.embed.swing.SwingNode props))

(defmacro pane [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.layout.Pane props))

(defmacro blend [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.effect.Blend props))

(defmacro quad-curve-to [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.QuadCurveTo props))

(defmacro bar-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.BarChart props))

(defmacro split-menu-button [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.SplitMenuButton props))

(defmacro web-engine [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.web.WebEngine props))

(defmacro password-field [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.PasswordField props))

(defmacro read-only-object-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.beans.property.ReadOnlyObjectWrapper props))

(defmacro text-field-tree-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.TextFieldTreeCell props))

(defmacro text [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.text.Text props))

(defmacro point-light [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.PointLight props))

(defmacro pagination [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Pagination props))

(defmacro tree-table-row [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeTableRow props))

(defmacro list-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ListView props))

(defmacro text-flow [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.text.TextFlow props))

(defmacro tree-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeItem props))

(defmacro popup [& {:as props}] (fn-fx.render-core/component-impl :javafx.stage.Popup props))

(defmacro image-input [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.ImageInput props))

(defmacro tool-bar [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ToolBar props))

(defmacro separator [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Separator props))

(defmacro canvas [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.canvas.Canvas props))

(defmacro table-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TableView props))

(defmacro color-adjust [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.ColorAdjust props))

(defmacro table-column [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TableColumn props))

(defmacro stage [& {:as props}] (fn-fx.render-core/component-impl :javafx.stage.Stage props))

(defmacro column-constraints [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.ColumnConstraints props))

(defmacro scene [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.Scene props))

(defmacro arc [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.shape.Arc props))

(defmacro menu-bar [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.MenuBar props))

(defmacro tree-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeTableCell props))

(defmacro glow [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.effect.Glow props))

(defmacro flow-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.FlowPane props))

(defmacro spinner [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Spinner props))

(defmacro table-row [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TableRow props))

(defmacro parallel-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.ParallelTransition props))

(defmacro accordion [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Accordion props))

(defmacro scale [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.transform.Scale props))

(defmacro progress-bar [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ProgressBar props))

(defmacro slider [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Slider props))

(defmacro stacked-area-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.StackedAreaChart props))

(defmacro combo-box-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ComboBoxTableCell props))

(defmacro color-picker [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ColorPicker props))

(defmacro read-only-boolean-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.beans.property.ReadOnlyBooleanWrapper props))

(defmacro fill-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.FillTransition props))

(defmacro table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TableCell props))

(defmacro media-player [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.media.MediaPlayer props))

(defmacro inner-shadow [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.InnerShadow props))

(defmacro context-menu [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ContextMenu props))

(defmacro pie-chart [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.chart.PieChart props))

(defmacro tree-table-column [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeTableColumn props))

(defmacro displacement-map [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.DisplacementMap props))

(defmacro dialog [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Dialog props))

(defmacro text-field [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TextField props))

(defmacro float-map [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.FloatMap props))

(defmacro cell [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.control.Cell props))

(defmacro sorted-list [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.collections.transformation.SortedList props))

(defmacro choice-box-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ChoiceBoxTableCell props))

(defmacro tile-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.TilePane props))

(defmacro check-box [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.CheckBox props))

(defmacro check-box-tree-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.CheckBoxTreeTableCell props))

(defmacro bloom [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.effect.Bloom props))

(defmacro rectangle [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.Rectangle props))

(defmacro toggle-group [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ToggleGroup props))

(defmacro hyperlink [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Hyperlink props))

(defmacro stroke-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.StrokeTransition props))

(defmacro tree-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TreeCell props))

(defmacro motion-blur [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.MotionBlur props))

(defmacro read-only-float-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.beans.property.ReadOnlyFloatWrapper props))

(defmacro indexed-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.IndexedCell props))

(defmacro region [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.layout.Region props))

(defmacro menu-button [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.MenuButton props))

(defmacro v-line-to [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.VLineTo props))

(defmacro line-to [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.LineTo props))

(defmacro group [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.Group props))

(defmacro split-pane [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.SplitPane props))

(defmacro text-area [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TextArea props))

(defmacro separator-menu-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.SeparatorMenuItem props))

(defmacro choice-box-list-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ChoiceBoxListCell props))

(defmacro read-only-integer-wrapper [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.beans.property.ReadOnlyIntegerWrapper props))

(defmacro perspective-transform [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.PerspectiveTransform props))

(defmacro close-path [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.ClosePath props))

(defmacro quad-curve [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.QuadCurve props))

(defmacro translate-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.TranslateTransition props))

(defmacro text-field-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.TextFieldTableCell props))

(defmacro check-box-tree-item [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.CheckBoxTreeItem props))

(defmacro progress-indicator [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.ProgressIndicator props))

(defmacro ambient-light [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.AmbientLight props))

(defmacro sub-scene [& {:as props}] (fn-fx.render-core/component-impl :javafx.scene.SubScene props))

(defmacro button [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.Button props))

(defmacro progress-bar-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ProgressBarTableCell props))

(defmacro parallel-camera [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.ParallelCamera props))

(defmacro text-input-dialog [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.TextInputDialog props))

(defmacro box-blur [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.effect.BoxBlur props))

(defmacro image-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.image.ImageView props))

(defmacro mesh-view [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.shape.MeshView props))

(defmacro progress-bar-tree-table-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.ProgressBarTreeTableCell props))

(defmacro fade-transition [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.animation.FadeTransition props))

(defmacro check-box-tree-cell [& {:as props}]
  (fn-fx.render-core/component-impl :javafx.scene.control.cell.CheckBoxTreeCell props))

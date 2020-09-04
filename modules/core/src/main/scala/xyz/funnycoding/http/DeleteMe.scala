package xyz.funnycoding.http
import xyz.funnycoding.domain.volume.Volume
import xyz.funnycoding.http.json._
import io.circe.parser.decode

object DeleteMe extends App {
  val s =
    s"""{
  "kind": "books#volume",
  "id": "TZqxDwAAQBAJ",
  "etag": "UUTckU+r8Yw",
  "selfLink": "https://www.googleapis.com/books/v1/volumes/TZqxDwAAQBAJ",
  "volumeInfo": {
    "title": "Quilting Patterns",
    "subtitle": "110 Ready-to-Use Machine Quilting Designs",
    "authors": [
      "Linda Macho"
    ],
    "publisher": "Courier Dover Publications",
    "publishedDate": "2019-12-18",
    "description": "Feathers, pinwheels, flowers, birds, and dozens of other eye-catching designs abound in this treasury of 110 quilting patterns. Linda Macho blends traditional motifs with her own original contemporary forms to offer a rich assortment of full-size templates. They can be used individually or in combinations to create an unlimited number of variations.\u003cbr\u003eClear, complete directions and numerous diagrams guide quilters of every skill level through the step-by-step process: the selection and use of patterns; frames, hoops, and other equipment; assembling a quilt; how to stitch; and much more! Macho even explains the making of a perforated pattern with a sewing machine, cutting stencils, and other techniques. Whether you are making a quilt for practical use, as a decorative bedcover, a wall hanging, or a gift, this wealth of designs and easy-to-follow instructions will prove a handy guide and a valuable reference.",
    "industryIdentifiers": [
      {
        "type": "ISBN_10",
        "identifier": "0486838153"
      },
      {
        "type": "ISBN_13",
        "identifier": "9780486838151"
      }
    ],
    "readingModes": {
      "text": false,
      "image": true
    },
    "pageCount": 80,
    "printedPageCount": 83,
    "dimensions": {
      "height": "27.60 cm",
      "width": "21.00 cm",
      "thickness": "0.50 cm"
    },
    "printType": "BOOK",
    "categories": [
      "Crafts & Hobbies / Quilts & Quilting",
      "Crafts & Hobbies / Sewing"
    ],
    "maturityRating": "NOT_MATURE",
    "allowAnonLogging": false,
    "contentVersion": "0.1.0.0.preview.1",
    "panelizationSummary": {
      "containsEpubBubbles": false,
      "containsImageBubbles": false
    },
    "imageLinks": {
      "smallThumbnail": "http://books.google.com/books/content?id=TZqxDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&imgtk=AFLRE71BgsO1KHF9Ns5yWzpYzNnBKh0Z6Og8Uh1DyMB3NxsRMWGNHCE-XdvkwxI0ZsbHbc4Us2IFvdSHERxz3eCuV3pz3-vZtPaUzl8UbWRJ216NdtvEwUMaY6FbRAqt6smuHX2kEZ3l&source=gbs_api",
      "thumbnail": "http://books.google.com/books/content?id=TZqxDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&imgtk=AFLRE719zFNSeAmUjIUlKlrL49wOE5lisgaz-N0JUkTPyqWvpy2KEhOZfQ9JqNwY_nIP7DlcxXgkEvoReWjAPkFY33KkMnugTmmWpFiPgnqeI-tT5LrBN_aqwJ93OlfaURzrpYjaGzTQ&source=gbs_api",
      "small": "http://books.google.com/books/content?id=TZqxDwAAQBAJ&printsec=frontcover&img=1&zoom=2&edge=curl&imgtk=AFLRE71RHeeGLjKdcdMFf10PcCz4BezJ_35enofmrXVTwluajT-SWMEvJBa7KJs0tdcydntKS3Dd4TVKxzNDyG3OGVJo4reCdw9eiVXcmgYT4eXmEqkcI0qc2qjJp7Ja6EYtTdms-vYU&source=gbs_api",
      "medium": "http://books.google.com/books/content?id=TZqxDwAAQBAJ&printsec=frontcover&img=1&zoom=3&edge=curl&imgtk=AFLRE73fUWo_x65PZna0RCCKqXiGzG2i9PJLwGNesvQ3I_zHI7HHk2D0QR-FIDMErAPSgZZM6SUINuPD9QrON3kY6WH7HAvX7lP7vdRXEK5rSP_rCO1nVloUGST0y-YZ_PF7jH5chKre&source=gbs_api",
      "large": "http://books.google.com/books/content?id=TZqxDwAAQBAJ&printsec=frontcover&img=1&zoom=4&edge=curl&imgtk=AFLRE71IKNIMaq5bJHhb0mTx5he4pXw9miX_7NOazZHn6PVBPCHx_QuVlPt-xv6ul17L5Wem5YuWy8ppk_HVwVBEWXj7m2wJKXrlJyphyNVObFT9HjZLrhobBGqlIK_u-KBJLG-YdiRY&source=gbs_api",
      "extraLarge": "http://books.google.com/books/content?id=TZqxDwAAQBAJ&printsec=frontcover&img=1&zoom=6&edge=curl&imgtk=AFLRE71tchkZ-0fJC_yt-qEA-S6hwtywjOI_mqFDxavv4fGcoIpvjm9lXkHsxv8htp75fgFsybcHDJYDb0SkY4iBc8fHvZj6UPzTsoTlB-qn57IibhEsiFQPX7s6k2ePqdn1vmF2f3y8&source=gbs_api"
    },
    "language": "ru",
    "previewLink": "http://books.google.fr/books?id=TZqxDwAAQBAJ&hl=&source=gbs_api",
    "infoLink": "https://play.google.com/store/books/details?id=TZqxDwAAQBAJ&source=gbs_api",
    "canonicalVolumeLink": "https://play.google.com/store/books/details?id=TZqxDwAAQBAJ"
  },
  "saleInfo": {
    "country": "FR",
    "saleability": "NOT_FOR_SALE",
    "isEbook": false
  },
  "accessInfo": {
    "country": "FR",
    "viewability": "PARTIAL",
    "embeddable": true,
    "publicDomain": false,
    "textToSpeechPermission": "ALLOWED",
    "epub": {
      "isAvailable": false
    },
    "pdf": {
      "isAvailable": false
    },
    "webReaderLink": "http://play.google.com/books/reader?id=TZqxDwAAQBAJ&hl=&printsec=frontcover&source=gbs_api",
    "accessViewStatus": "SAMPLE",
    "quoteSharingAllowed": false
  }
}
"""

  println(decode[Volume](s))
}

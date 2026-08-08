import qrcode
import sys
from datetime import datetime
from pathlib import Path

QR_CODE_PATH = Path(r"C:\Users\Ayoub\Desktop\url-shortener\src\main\resources\static\QRCodes")

def get_timestamp() -> str:
    return datetime.now().strftime("%Y%m%d%H%M%S_%f")


def generate_qr_code(data : str) -> None:
    QR_CODE_PATH.mkdir(parents=True, exist_ok=True)

    img = qrcode.make(data)
    qrcode_name = f"qrcode-{get_timestamp()}.png"
    full_path = QR_CODE_PATH / qrcode_name
    img.save(full_path)

    print(f"Log: Saved to {full_path}", file=sys.stderr)

    return full_path


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Error: Please provide data for the QR code.", file=sys.stderr)
        sys.exit(1)
    final_path = generate_qr_code(sys.argv[1])
    print(final_path.resolve())
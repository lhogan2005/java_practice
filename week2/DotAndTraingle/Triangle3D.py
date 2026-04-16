import math

class Triangle3D:
    def __init__(self, dot1, dot2, dot3):
        self.dot1 = dot1
        self.dot2 = dot2
        self.dot3 = dot3
        self.edge1 = self.dot1.distance_to(self.dot2)
        self.edge2 = self.dot2.distance_to(self.dot3)
        self.edge3 = self.dot3.distance_to(self.dot1)

    def calculate_perimeter(self):
        perimeter = self.edge1 + self.edge2 + self.edge3
        return perimeter

    def calculate_area(self):
        per = self.calculate_perimeter() / 2
        area = math.sqrt(per * (per - self.edge1) * (per - self.edge2) * (per - self.edge3))
        return area

class Dot3D:
    def __init__(self, x, y, z, label = None):
        self.x = x
        self.y = y
        self.z = z
        self.label = label
        
    def distance_to(self, other):
        distance = math.sqrt((other.x - self.x)**2 + (other.y - self.y)**2 + (other.z - self.z)**2)
        return distance

    def add_vector(self, other):
        new_x = other.x + self.x
        new_y = other.y + self.y
        new_z = other.z + self.z
        new_label = self.label + "+" + other.label
        return Dot3D(new_x, new_y, new_z, new_label)